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
import java.util.logging.Logger;

import org.l2jmobius.gameserver.handler.IVoicedCommandHandler;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.Summon;

import sskim.faker.Faker;
import sskim.faker.FakerManager;
import sskim.faker.FakerService;
import sskim.faker.enums.FakerJob;

/**
 * @author sskim
 */
public class CreateFaker implements IVoicedCommandHandler
{
	private static final Logger LOGGER = Logger.getLogger(CreateFaker.class.getName());
	private static final String[] VOICED_COMMANDS =
	{
		"생성",
		"생성랜덤",
		"삭제"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String param)
	{
		if (!Arrays.asList(VOICED_COMMANDS).contains(command) || (player == null))
		{
			return false;
		}
		
		/**************** 삭제 부분 ****************/
		
		if(command.equals("삭제")) {
			Creature targetCr = (Creature) player.getTarget();
			
			if (targetCr == null || (!(targetCr instanceof Faker) && !(targetCr instanceof Summon))) {
			    player.sendMessage("삭제할 FakePlayer를 먼저 선택하세요.");
			    return false;
			}
			
			boolean success = FakerManager.getInstance().removeFaker(targetCr.getObjectId());
			if (success)
			{
				player.sendMessage("Faker " + targetCr.getName() + " 가 삭제되었습니다.");
			}
			else
			{
				player.sendMessage("Faker 삭제에 실패했습니다.");
			}
			
			return true;
		}
		
		/**************** 생성 부분 ****************/
		
		if ((param == null) || param.isBlank())
		{
			sendUsageMessage(player);
			return false;
		}
		
		String[] params = param.split(" ");
		
		if ((params.length == 0) || (params.length > 2))
		{
			sendUsageMessage(player);
			return false;
		}
		
		String job = params[0];
		String sex = ((params.length == 2) && params[1].equals("여")) ? "여" : "남";
		
		if (!isValidJob(job))
		{
			player.sendMessage("올바른 직업을 입력하세요: " + FakerJob.getValidJobNames());
			LOGGER.warning("Faker 생성 실패: 잘못된 직업 입력 (" + job + ")");
			return false;
		}
		
		Faker faker = FakerService.getInstance().createFaker(player, job, sex);
		
		if (faker != null)
		{
			player.sendMessage("Faker가 성공적으로 생성되었습니다: " + job + " " + sex);
		}
		else
		{
			player.sendMessage("Faker 생성에 실패했습니다.");
			return false;
		}
		
		if (command.equals("생성랜덤"))
		{
			// 랜덤한 케릭터 생성
		}
		
		return true;
	}
	
	// 사용법 안내 메시지 출력
	private void sendUsageMessage(Player player)
	{
		player.sendMessage("사용법: .생성 [직업] [성별] **성별 생략시 기본값(남)**");
		player.sendMessage("[직업]: " + FakerJob.getValidJobNames());
	}
	
	private boolean isValidJob(String job)
	{
		return FakerJob.getClassIdByName(job) != -1;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
