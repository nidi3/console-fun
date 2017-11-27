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
import java.util.Arrays;

import static guru.nidi.consolefun.AnsiFun.flushOut;
import static guru.nidi.consolefun.Key.ESC;
import static guru.nidi.consolefun.Key.NONE;

public class DirectKeyModeTest {
    public static void main(String[] args) throws IOException {
        new DirectKeyModeTest().test();
    }

    void test() throws IOException {
        new DirectKeyMode().run(dkm -> {
            System.out.println(Arrays.toString(dkm.cursorPos()));
            System.out.println(Arrays.toString(dkm.screenSize()));
            while (true) {
                Key key = dkm.key();
                if (key.code == ESC) {
                    break;
                }
                if (key != NONE) {
                    flushOut(" " + (int) key.code);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
