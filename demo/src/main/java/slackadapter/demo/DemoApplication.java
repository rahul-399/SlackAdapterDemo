package slackadapter.demo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {
	private static String bot_token = "xoxb-4618763440435-4616000102373-N7bM7ps7bDfZBdfmSFbJ0s8u";
	private static MethodsClient client = Slack.getInstance().methods();
	
	public static void main(String[] args) throws InterruptedException {
		System.out.print("Enter Name of the Channel: ");
		Scanner sc = new Scanner(System.in);
		String channelName = sc.nextLine();
		String channelId = createConversation(channelName);
		if(!channelId.equals("")){
			System.out.println("\n\n Channel Created -> Id:"+ channelId + "  Name: "+channelName);
			boolean joinConversationResp = joinConversation(channelId);
			if(joinConversationResp == true){
				System.out.print("Enter Message: ");
				String message = sc.nextLine();  
				boolean sendMessageResp = sendMessage(channelName, message);
				System.out.println("Message Response: "+ sendMessageResp);
			}
		}
		listConversations();
		listUsers();
	}
	
	static String createConversation(String channelName){
		try {
			var result = client.conversationsCreate(r->r
			.token(bot_token)
			.name(channelName)
			);
			if(result.isOk() == true){
				return result.getChannel().getId();
			}
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
			return new String("");
        }
		return new String("");
	}

	static void listConversations(){
		try {
            var result = client.conversationsList(r -> r
                .token(bot_token)
            );
			System.out.println(result);
			System.out.println("\n\n\n\n\nList of channels: ");
            for (Conversation channel : result.getChannels()) {
				var conversationId = channel.getId();
				var conversationName = channel.getName();
				System.out.println("Channel ID: "+conversationId+"    Name: "+ conversationName);
            }
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
        }
	}

	static boolean joinConversation(String channelId){
		try {
			var result = client.conversationsJoin(r->r
			.token(bot_token)
			.channel(channelId)
			);
			System.out.println(result);
			return result.isOk();
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
			return false;
        }
	}

	static boolean sendMessage(String channelId, String message){
		try {
			var result = client.chatPostMessage(r->r
			.token(bot_token)
			.channel(channelId)
			.text(message)
			);	
			return result.isOk();
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
			return false;
        }
	}

	static void listUsers(){
		try {
            var result = client.usersList(r -> r
                .token(bot_token)
            );
			System.out.println("\n\n\n\n\nList of Users: ");
            for (User user : result.getMembers()) {
				var userId = user.getId();
				var userName = user.getName();
				System.out.println("User ID: "+userId+"    Name: "+ userName);
            }
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
        }
	}

	static boolean channelHistory(String channelId){
		try {
			var result = client.conversationsHistory(r->r
			.token(bot_token)
			.channel(channelId)
			);
			return result.isOk();
        } catch (IOException | SlackApiException e) {
			System.out.println("error: "+ e.getMessage());
			return false;
        }
	}
}
