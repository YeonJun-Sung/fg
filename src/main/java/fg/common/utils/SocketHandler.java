package fg.common.utils;

import java.util.ArrayList;
import java.util.List;
 
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
 
import javax.websocket.RemoteEndpoint.Basic;
 
@Controller
@ServerEndpoint(value="/echo.do")
public class SocketHandler {
    private static final List<Session> sessionList = new ArrayList<Session>();
    Logger log = Logger.getLogger(this.getClass());
    
    public SocketHandler() {
        // TODO Auto-generated constructor stub
        System.out.println("웹소켓(서버) 객체생성 ");
    }
    
    @RequestMapping(value="/chat.do")
    public ModelAndView getChatViewPage(ModelAndView mav) {
        mav.setViewName("chat");
        return mav;
    }
    
    @OnOpen
    public void onOpen(Session session) {
        log.info("Open session id:" + session.getId());
        try {
            final Basic basic = session.getBasicRemote();
            basic.sendText("Connection Established");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        sessionList.add(session);
        sendAllSessionToMessage(session, "Enter," + session.getQueryString());
    }
    
    private void sendAllSessionToMessage(Session self, String message) {
        try {
            for(Session session : SocketHandler.sessionList) {
                if(!self.getId().equals(session.getId())) {
                    session.getBasicRemote().sendText(message);
                }
            }
        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Message From " + message);
        try {
            final Basic basic = session.getBasicRemote();
            basic.sendText("to : " + message);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        sendAllSessionToMessage(session, message);
    }
    
    @OnError
    public void onError(Throwable e, Session session) {
        log.debug("Chat Error : " + e.getMessage());
        log.debug("Chat Error id : " + session.getId());
    }
    
    @OnClose
    public void onClose(Session session) {
        log.debug("Session " + session.getId() + " has ended");
        sessionList.remove(session);
    }
}