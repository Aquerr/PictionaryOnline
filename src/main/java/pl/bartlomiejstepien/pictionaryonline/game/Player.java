package pl.bartlomiejstepien.pictionaryonline.game;

import lombok.Data;

@Data
public class Player
{
    private String name;
    private String color;
    private int prevX;
    private int prevY;
    private int currX;
    private int currY;
    private int lineWidth;

//        this.color = "black";
//    this.prevX = 0;
//    this.prevY = 0;
//    this.currX = 0;
//    this.currY = 0;
//    this.lineWidth = 2;
}
