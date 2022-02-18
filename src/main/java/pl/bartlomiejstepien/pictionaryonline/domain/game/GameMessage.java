package pl.bartlomiejstepien.pictionaryonline.domain.game;

import lombok.Data;

@Data
public class GameMessage
{
    private String name;
    private String color;
    private int prevX;
    private int prevY;
    private int currX;
    private int currY;
    private int lineWidth;
}
