package Server.Websocket.Server.Web;

import Server.Websocket.Server.Web.Util.MessageDecoder;
import Server.Websocket.Server.Web.Util.MessageEncoder;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeverEndPointConfig implements ServerEndpointConfig {

    private final Map<String,Object> userProperties = new HashMap<>();
    @Override
    public Class<?> getEndpointClass() {
        return SeverEndPointConfig.class;
    }

    @Override
    public String getPath() {
        return "/game/bingo";
    }

    @Override
    public List<String> getSubprotocols() {
        return null;
    }

    @Override
    public List<Extension> getExtensions() {
        return null;
    }

    @Override
    public Configurator getConfigurator() {
        return null;
    }

    @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return  Arrays.asList(MessageEncoder.class);
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return Arrays.asList(MessageDecoder.class);
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return userProperties;
    }
}
