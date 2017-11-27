/*
 * Copyright © 2017 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.consolefun;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

import static guru.nidi.consolefun.AnsiFun.ansi;
import static guru.nidi.consolefun.AnsiFun.flushOut;
import static guru.nidi.consolefun.Color.DEFAULT;
import static guru.nidi.consolefun.Key.ESC;
import static guru.nidi.consolefun.Key.NONE;

public class DirectKeyMode {
    private BlockingDeque<Key> keys = new LinkedBlockingDeque<>();

    public void run(Consumer<DirectKeyMode> action) throws IOException {
        String ttyConfig = stty("-g");
        try {
            init();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> reset(ttyConfig)));
            Thread keyListener = new Thread(() -> {
                while (true) {
                    try {
                        handleKeyInput();
                        Thread.sleep(20);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            keyListener.setDaemon(true);
            keyListener.start();
            action.accept(this);
        } finally {
            reset(ttyConfig);
        }
    }

    protected void init() throws IOException {
        stty("-icanon min 1"); // set the console to be character-buffered instead of line-buffered
        stty("-echo"); // disable character echoing
        flushOut(ansi().hideCursor());
    }

    protected void reset(String ttyConfig) {
        try {
            stty(ttyConfig.trim());
            flushOut(ansi().showCursor().color(DEFAULT.fg()).color(DEFAULT.bg()));
        } catch (IOException e) {
            //ignore
        }
    }

    public Key key() {
        final Key key = keys.poll();
        return key == null ? NONE : key;
    }

    public int[] screenSize() {
        flushOut(ansi().saveCursorPosition().cursorDown(1000).cursorRight(1000));
        final int[] pos = cursorPos();
        flushOut(ansi().restoreCursorPosition());
        return pos;
    }

    public int[] cursorPos() {
        flushOut(ansi().esc("6n"));
        Key pos = null;
        try {
            pos = keys.takeFirst();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String[] parts = pos.ansiInfo.split(";");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    private void handleKeyInput() throws IOException {
        if (System.in.available() != 0) {
            char key = readIn();
            if (key == ESC) {
                if (System.in.available() == 0) {
                    keys.add(Key.key(ESC));
                } else {
                    char sec = readIn();
                    if (sec == '[') {
                        String buf = "";
                        do {
                            key = readIn();
                            buf += (char) key;
                        } while (key != '~' && (key < 'A' || key > 'Z') && (key < 'a' || key > 'z'));
                        keys.add(Key.ansi(buf.charAt(buf.length() - 1), buf.substring(0, buf.length() - 1)));
                    } else {
                        keys.add(Key.key(ESC));
                        keys.add(Key.key(sec));
                    }
                }
            } else {
                keys.add(Key.key(key));
            }
        }
    }

    private char readIn() throws IOException {
        return (char) System.in.read();
    }

    private static String stty(final String args) throws IOException {
        return Shell.exec("sh", "-c", "stty " + args + " < /dev/tty").outString();
    }

}

