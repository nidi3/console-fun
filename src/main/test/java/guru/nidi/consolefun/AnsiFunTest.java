package guru.nidi.consolefun;

import org.junit.jupiter.api.Test;

import static guru.nidi.consolefun.AnsiFun.ansi;
import static guru.nidi.consolefun.AnsiFun.flushOut;
import static guru.nidi.consolefun.Color.*;

public class AnsiFunTest {
    @Test
    void colors() {
        System.out.println(ansi()
                .color(BLACK).a("BLACK").color(BLUE).a("BLUE").color(CYAN).a("CYAN").color(DEFAULT).a("DEFAULT")
                .color(GREEN).a("GREEN").color(MAGENTA).a("MAGENTA").color(RED).a("RED").color(WHITE).a("WHITE").color(YELLOW).a("YELLOW"));
        System.out.println(ansi()
                .bg(BLACK).a("BLACK").bg(BLUE).a("BLUE").bg(CYAN).a("CYAN").bg(DEFAULT).a("DEFAULT")
                .bg(GREEN).a("GREEN").bg(MAGENTA).a("MAGENTA").bg(RED).a("RED").bg(WHITE).a("WHITE").bg(YELLOW).a("YELLOW"));
        System.out.println(ansi()
                .color(BLACK.bright()).a("BLACK").color(BLUE.bright()).a("BLUE").color(CYAN.bright()).a("CYAN").color(DEFAULT.bright()).a("DEFAULT")
                .color(GREEN.bright()).a("GREEN").color(MAGENTA.bright()).a("MAGENTA").color(RED.bright()).a("RED").color(WHITE.bright()).a("WHITE").color(YELLOW.bright()).a("YELLOW"));
        System.out.println(ansi()
                .bg(BLACK.bright()).a("BLACK").bg(BLUE.bright()).a("BLUE").bg(CYAN.bright()).a("CYAN").bg(DEFAULT.bright()).a("DEFAULT")
                .bg(GREEN.bright()).a("GREEN").bg(MAGENTA.bright()).a("MAGENTA").bg(RED.bright()).a("RED").bg(WHITE.bright()).a("WHITE").bg(YELLOW.bright()).a("YELLOW"));
        System.out.println(ansi().fg(DEFAULT).bg(DEFAULT));
    }

    @Test
    void cursor() {
        flushOut(ansi().a("12345").cursorRight(2).a("67890").cursorLeft(8).a("xx"));
        flushOut(ansi().cursorUp(1).a("yyyy").cursorDown(2).a("zzz"));
    }
}
