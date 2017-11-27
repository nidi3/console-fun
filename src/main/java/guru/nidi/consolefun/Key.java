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

public class Key {
    public static final Key NONE = new Key((char) 0, (char) 0, null);

    static final char ESC = 27;
    public final char code;
    final char ansi;
    final String ansiInfo;

    Key(char code, char ansi, String ansiInfo) {
        this.code = code;
        this.ansi = ansi;
        this.ansiInfo = ansiInfo;
    }

    static Key key(char code) {
        return new Key(code, (char) 0, null);
    }

    static Key ansi(char type, String info) {
        return new Key((char) 0, type, info);
    }

    public boolean isLeft() {
        return ansi == 'D';
    }

    public boolean isRight() {
        return ansi == 'C';
    }

    public boolean isUp() {
        return ansi == 'A';
    }

    public boolean isDown() {
        return ansi == 'B';
    }

    public boolean isEsc() {
        return code == ESC;
    }

    public boolean isEnter() {
        return code == 10;
    }

    public boolean isBackspace() {
        return code == 127;
    }

    public boolean isHome() {
        return isTilde(1);
    }

    public boolean isInsert() {
        return isTilde(2);
    }

    public boolean isDelete() {
        return isTilde(3);
    }

    public boolean isEnd() {
        return isTilde(4);
    }

    public boolean isPageUp() {
        return isTilde(5);
    }

    public boolean isPageDown() {
        return isTilde(6);
    }

    public boolean isBasic() {
        return code >= ' ' && code < 127;
    }

    private boolean isTilde(int code) {
        return ansi == '~' && Integer.toString(code).equalsIgnoreCase(ansiInfo);
    }

    @Override
    public String toString() {
        return code > 0 ? Integer.toString(code) : ("ansi(" + ansiInfo + ")" + ansi);
    }
}
