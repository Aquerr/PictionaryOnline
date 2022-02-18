package pl.bartlomiejstepien.pictionaryonline.interfaces.web.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@AllArgsConstructor
public class GameController
{
    @MessageMapping(value = "/action")
    @SendTo("/topic/action")
    public ObjectNode message(@Payload final ObjectNode message)
    {
        return message;
    }
}
