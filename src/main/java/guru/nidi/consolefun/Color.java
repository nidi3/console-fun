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

public class Color {
    public static final Color BLACK = new Color(0);
    public static final Color RED = new Color(1);
    public static final Color GREEN = new Color(2);
    public static final Color YELLOW = new Color(3);
    public static final Color BLUE = new Color(4);
    public static final Color MAGENTA = new Color(5);
    public static final Color CYAN = new Color(6);
    public static final Color WHITE = new Color(7);
    public static final Color DEFAULT = new Color(9);

    private final int value;
    private final boolean fore;
    private final boolean bright;

    private Color(int value) {
        this(value, true, false);
    }

    private Color(int value, boolean fore, boolean bright) {
        this.value = value;
        this.fore = fore;
        this.bright = bright;
    }

    public Color fg() {
        return new Color(value, true, bright);
    }

    public Color bg() {
        return new Color(value, false, bright);
    }

    public Color bright() {
        return new Color(value, fore, true);
    }

    public Color dark() {
        return new Color(value, fore, false);
    }

    int value() {
        return value + (fore ? 30 : 40) + (bright ? 60 : 0);
    }
}
