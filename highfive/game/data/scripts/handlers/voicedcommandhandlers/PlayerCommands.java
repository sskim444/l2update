/*
 * Copyright (c) 2013 L2jMobius
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package handlers.voicedcommandhandlers;

import java.util.Arrays;

import org.l2jmobius.gameserver.handler.IVoicedCommandHandler;
import org.l2jmobius.gameserver.model.actor.Player;

import sskim.faker.FakerClanManager;
import sskim.faker.FakerManager;
import sskim.utils.PlayerCommand;

/**
 * @author sskim
 */
public class PlayerCommands implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"ㄱ",
		"ㄱㄱ",
		"ㄱㄱㄱ",
		"ㅋ",
		"ㅋㅋ",
		"ㅋㅋㅋ",
		"어시",
		"어시해제",
		"모여",
		"마을로",
		"ai초기화",
		"소환",
		"ai",
		"ai4",
		"스킬시전",
		"가입"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String param)
	{
		if ((player == null) || !Arrays.asList(VOICED_COMMANDS).contains(command))
		{
			return false;
		}
		
		if (command.equals("ㄱㄱㄱ"))
		{
			if(PlayerCommand.goAttack(player)) {
				player.sendMessage("일제히 공격합니다!");
				return true;
			}
			return false;
		}
		else if (command.equals("ㄱㄱ"))
		{
			PlayerCommand.gogo(player);
			player.sendMessage("공격을 재개합니다!!");
			return true;
		}
		else if (command.equals("ㄱ"))
		{
			PlayerCommand.gogo(player);
			player.sendMessage("공격을 재개합니다!!");
			return true;
		}
		else if (command.equals("어시"))
		{
			Player assist = FakerManager.getInstance().whoAssistPartyMember(player);
			player.sendMessage("현재 파티원 중 어시케릭터: " + (assist == null ? "없음" : assist.getName()));
			
			if (player.getTarget() == null)
			{
				player.sendMessage("어시스트 케릭터를 선택하세요.");
				return false;
			}
			
			PlayerCommand.setAssistMember(player, player.getTarget().getName());
			player.sendMessage(player.getTarget().getName() + "님이 어시스트 케릭터로 설정되었습니다.");
			
			assist = FakerManager.getInstance().whoAssistPartyMember(player);
			player.sendMessage("현재 파티원 중 어시케릭터: " + (assist == null ? "없음" : assist.getName()));
			
			return true;
		}
		else if (command.equals("어시해제"))
		{
			PlayerCommand.delAssistMember(player);
			Player assist = FakerManager.getInstance().whoAssistPartyMember(player);
			player.sendMessage("어시스트가 해제되었습니다.");
			player.sendMessage("현재 파티원 중 어시케릭터: " + (assist == null ? "없음" : assist.getName()));
			return true;
		}
		else if (command.equals("모여"))
		{
			PlayerCommand.followMe(player);
			player.sendMessage("공격을 중지하고 모여주세요!!");
			return true;
		}
		else if (command.equals("마을로")) {
			if(PlayerCommand.toVillage(player)) {
				player.sendMessage("죽은 사람은 모두 마을로~~");
				return true;
			}
			player.sendMessage("마을에서만 실행할 수 있습니다.");
			return false;
		} else if(command.equals("ai초기화")) {
			String[] params = param.split(" ");
			
			if ((params.length == 0) || (params.length > 1))
			{
				player.sendMessage("사용법: .ai초기화 [케릭터명]");;
				return false;
			}
			
			if(PlayerCommand.aiInitialize(player, params[0])) {
				player.sendMessage("AI가 성공적으로 초기화되었습니다.");
				return true;
			}
			player.sendMessage("AI 초기화에 실패하였습니다.");
			
			return false;
		} else if(command.equals("소환")) {
			String[] params = param.split(" ");
			
			if ((params.length == 0) || (params.length > 1)) {
				player.sendMessage("사용법: .소환 [케릭터명]");
				return false;
			}
			
			if(PlayerCommand.recall(player, params[0])) {
				player.sendMessage("성공적으로 소환되었습니다.");
				return true;
			}
			player.sendMessage("소환에 실패하였습니다.");
			return false;
			
		} else if (command.equals("ai")) {
			if(PlayerCommand.startAi()) player.sendMessage("AI가 시작되었습니다.");
			return true;
		} else if(command.equals("ai4")) {
			if(PlayerCommand.stopAi()) player.sendMessage("AI가 중단되었습니다.");
			return true;
		} else if(command.equals("스킬시전")) {
			String[] params = param.split(" ");
			if(params.length < 1 && !params[0].matches("\\d+")) return false;
			
			PlayerCommand.castSkill(player.getTarget(), params[0]);
			return true;
		} else if(command.equals("가입")) {
			FakerClanManager.joinCustom(null, player);
			player.sendMessage("가입되었습니다.");
			return true;
		}
		
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
	
}
