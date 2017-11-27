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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Shell {
    private Shell() {
    }

    public static Result exec(final String... cmd) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ByteArrayOutputStream berr = new ByteArrayOutputStream();
        Process p = Runtime.getRuntime().exec(cmd);
        copy(p.getInputStream(), bout);
        copy(p.getErrorStream(), berr);
        try {
            final int code = p.waitFor();
            return new Result(code, bout.toByteArray(), berr.toByteArray());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
    }

    public static class Result {
        private final int code;
        private final byte[] out;
        private final byte[] err;

        Result(int code, byte[] out, byte[] err) {
            this.code = code;
            this.out = out;
            this.err = err;
        }

        public int code() {
            return code;
        }

        public byte[] outBytes() {
            return out;
        }

        public byte[] errBytes() {
            return err;
        }

        public String outString() {
            return new String(out);
        }

        public String errString() {
            return new String(err);
        }
    }
}
