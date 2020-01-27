import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MiniMidiMusicApp{

    static JFrame f = new JFrame("My first music video");
    static MyDrawPanel ml;

    public static void main(String[] args) {
        MiniMidiMusicApp mini = new MiniMidiMusicApp();
       mini.go();
    }

    public void setUpGui(){
        ml = new MyDrawPanel();
        f.setContentPane(ml);
        f.setBounds(30, 30, 300, 30);
        f.setVisible(true);
    }


    private void go() {
        setUpGui();

        try{
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            player.addControllerEventListener(ml, new int[] {127});
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            int r = 0;
            for(int i = 5; i < 60; i += 4){
                r = (int)((Math.random() * 50) + 1);
                track.add(makeEvent(144, 1, r, 100, i)); //the random music note start
                track.add(makeEvent(176, 1, 127, 0, i)); //176 mean that the event`s type is ControllerEvent
                track.add(makeEvent(128, 1, r, 100, i + 2)); //the random music note finish
            }
            player.setSequence(seq);
            player.setTempoInBPM(120);
            player.start();

        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public static MidiEvent makeEvent(int comb, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comb, chan, one, two);
            event = new MidiEvent(a, tick);
        }catch (Exception ex){}
        return event;
    }

    private static class MyDrawPanel extends JPanel implements ControllerEventListener{
        boolean msg = false;

        @Override
        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }

        public void paintComponent(Graphics g){
            if(msg){
                Graphics2D g2 = (Graphics2D)g;

                int r = (int)(Math.random() * 250);
                int gr = (int)(Math.random() * 250);
                int b = (int)(Math.random() * 250);

                g.setColor(new Color(r, gr, b));

                int ht = (int)((Math.random() * 120) + 10);
                int width = (int)((Math.random() * 120) + 10);

                int x = (int)((Math.random() * 40) + 10);
                int y = (int)((Math.random() * 40) + 10);

                g.fillRect(x, y, ht, width);

            }
        }
    }
}
