package cn.chu.com;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @program: IdeaProjects
 * @description:
 * @author: ChuChu
 * @create: 2021-11-15
 **/
public class BeatBox {
    JPanel mainPanel;
    JFrame theFrame;
    ArrayList<JCheckBox> checkboxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat",
            "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
            "Open Hi Conga"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String args[]){
        new BeatBox().buildGUI();
    }
    public void buildGUI(){
        theFrame = new JFrame();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkboxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(int i = 0; i < instrumentNames.length; i ++){
            nameBox.add(new Label(instrumentNames[i]));
        }
        //left
        background.add(BorderLayout.WEST, nameBox);
        //right
        background.add(BorderLayout.EAST, buttonBox);
        theFrame.getContentPane().add(background);

        //grid layout--->16 x 16 grid
        GridLayout grid = new GridLayout(16, 16);
        //set the vertical distance between two grids
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        for(int i = 0; i < 256; i ++){
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }
        //setUpMidi();

        theFrame.setSize(400, 500);
        theFrame.setVisible(true);
    }

    public void setUpMidi(){
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart(){
        int [] trackList = null;
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for(int i = 0; i < 16; i++){
            trackList = new int[16];
            int key = instruments[i];

            for(int j = 0; j < 16; j ++){
                JCheckBox jc = (JCheckBox) checkboxList.get(j + (16*i));
                if(jc.isSelected()){
                    trackList[j] = key;
                }else{
                    trackList[j] = 0;
                }
            }
            //makeTracks(trackList);
            //track.add(makeEvent(176,1,127,0,16));
        }
        //track.add(makeEvent(192,9,1,0,15));
        try {
            sequencer.setSequence(sequence);
            //sequencer.setLoopCount(sequencer.L);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

    }


    //four internal class
    public class MyStartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public class MyStopListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public class MyUpTempoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public class MyDownTempoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
