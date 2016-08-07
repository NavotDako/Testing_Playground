package FrameWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by navot.dako on 6/14/2016.
 */
public class Command {
    public List<Long> timeList = new ArrayList<>();
    public long totalTime = 0;
    public double avgTime = 0;
    public String commandName="";
    public Command (String commandName){
        this.commandName = commandName;
    }

}