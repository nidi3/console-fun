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

import java.util.ArrayList;
import java.util.List;

import static guru.nidi.consolefun.AnsiFun.Attribute.RESET;
import static guru.nidi.consolefun.AnsiFun.Erase.ALL;

public class AnsiFun {
    public enum Erase {
        FORWARD(0),
        BACKWARD(1),
        ALL(2);

        private final int value;

        Erase(int value) {
            this.value = value;
        }
    }

    public enum Attribute {
        RESET(0),
        BOLD_ON(1),
        FAINT_ON(2),
        ITALIC_ON(3),
        UNDERLINE_ON(4),
        BLINK_SLOW(5),
        BLINK_FAST(6),
        INVERSE_ON(7),
        CONCEAL(8),
        STRIKETHROUGH_ON(9),
        UNDERLINE_DOUBLE_ON(21),
        BOLD_OFF(22),
        ITALIC_OFF(23),
        UNDERLINE_OFF(24),
        BLINK_OFF(25),
        INVERSE_OFF(27),
        CONCEAL_OFF(28),
        STRIKETHROUGH_OFF(29);

        private final int value;

        Attribute(int value) {
            this.value = value;
        }
    }

    private static final char ESC1 = 27;
    private static final char ESC2 = '[';

    private final StringBuilder builder = new StringBuilder();
    private final List<Integer> attrs = new ArrayList<>(5);

    private AnsiFun() {
    }

    public static AnsiFun ansi() {
        return new AnsiFun();
    }

    public static void out(Object s) {
        System.out.print(s);
    }

    public static void flushOut(Object s) {
        System.out.print(s);
        System.out.flush();
    }

    public AnsiFun color(Color c) {
        attrs.add(c.value());
        return this;
    }

    public AnsiFun bg(Color c) {
        attrs.add(c.bg().value());
        return this;
    }

    public AnsiFun fg(Color c) {
        attrs.add(c.fg().value());
        return this;
    }

    public AnsiFun cursor(int y, int x) {
        return esc('H', x, y);
    }

    public AnsiFun cursorCol(int x) {
        return esc('G', x);
    }

    public AnsiFun cursorUp(int y) {
        return esc('A', y);
    }

    public AnsiFun cursorDown(int y) {
        return esc('B', y);
    }

    public AnsiFun cursorRight(int x) {
        return esc('C', x);
    }

    public AnsiFun cursorLeft(int x) {
        return esc('D', x);
    }

    public AnsiFun cursorDownline() {
        return esc('E');
    }

    public AnsiFun cursorDownLine(int n) {
        return esc('E', n);
    }

    public AnsiFun cursorUpLine() {
        return esc('F');
    }

    public AnsiFun cursorUpLine(int n) {
        return esc('F', n);
    }

    public AnsiFun eraseScreen() {
        return esc('J', ALL.value);
    }

    public AnsiFun eraseScreen(Erase kind) {
        return esc('J', kind.value);
    }

    public AnsiFun eraseLine() {
        return esc('K');
    }

    public AnsiFun eraseLine(Erase kind) {
        return esc('K', kind.value);
    }

    public AnsiFun scrollUp(int rows) {
        return esc('S', rows);
    }

    public AnsiFun scrollDown(int rows) {
        return esc('T', rows);
    }

    //TODO not sure if this is working
    public AnsiFun saveCursorPosition() {
        builder.append(ESC1).append('7');
        return esc('s');
    }

    //TODO not sure if this is working
    public AnsiFun restoreCursorPosition() {
        builder.append(ESC1).append('8');
        return esc('u');
    }

    public AnsiFun reset() {
        return a(RESET);
    }

    public AnsiFun hideCursor() {
        return esc("?25l");
    }

    public AnsiFun showCursor() {
        return esc("?25h");
    }

    public AnsiFun a(String value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(boolean value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(char value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(char[] value, int offset, int len) {
        flushAttributes();
        builder.append(value, offset, len);
        return this;
    }

    public AnsiFun a(char[] value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(CharSequence value, int start, int end) {
        flushAttributes();
        builder.append(value, start, end);
        return this;
    }

    public AnsiFun a(CharSequence value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(double value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(float value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(int value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(long value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(Object value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun a(StringBuffer value) {
        flushAttributes();
        builder.append(value);
        return this;
    }

    public AnsiFun newline() {
        flushAttributes();
        builder.append(System.getProperty("line.separator"));
        return this;
    }

    public AnsiFun format(String pattern, Object... args) {
        flushAttributes();
        builder.append(String.format(pattern, args));
        return this;
    }

    @Override
    public String toString() {
        flushAttributes();
        return builder.toString();
    }

    private AnsiFun flushAttributes() {
        if (!attrs.isEmpty()) {
            if (attrs.size() == 1 && attrs.get(0) == 0) {
                builder.append(ESC1).append(ESC2).append('m');
            } else {
                esc('m', attrs.toArray());
            }
            attrs.clear();
        }
        return this;
    }

    private AnsiFun esc(char command) {
        flushAttributes();
        builder.append(ESC1).append(ESC2).append(command);
        return this;
    }

    AnsiFun esc(String command) {
        flushAttributes();
        builder.append(ESC1).append(ESC2).append(command);
        return this;
    }

    private AnsiFun esc(char command, Object... options) {
        builder.append(ESC1).append(ESC2);
        for (int i = 0; i < options.length; i++) {
            if (i != 0) {
                builder.append(';');
            }
            if (options[i] != null) {
                builder.append(options[i]);
            }
        }
        builder.append(command);
        return this;
    }

}
