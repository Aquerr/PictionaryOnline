package pl.bartlomiejstepien.pictionaryonline.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@AllArgsConstructor
public class GameController
{
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping(value = "/action")
    @SendTo("/topic/action")
    public ObjectNode message(@Payload final ObjectNode message) throws Exception
    {
//        log.info("Received Player object = " + player);
        return message;
    }
}
