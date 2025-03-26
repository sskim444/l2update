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

import org.l2jmobius.gameserver.handler.IVoicedCommandHandler;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Player;

import sskim.faker.Helpers.Buffs;

/**
 * @author sskim
 */
public class ApplyBuffs implements IVoicedCommandHandler
{
	private static final String COMMAND = "버프";
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		if (!command.equalsIgnoreCase(COMMAND) || (player == null))
		{
			return false;
		}
		
		Creature targetCreature = (Creature) player.getTarget();
		
		if (targetCreature == null)
		{
			player.sendMessage("버프 받을 케릭터를 선택해주세요.");
			return false;
		}
		
		if (targetCreature instanceof Player)
		{
			Player targetPlayer = (Player) targetCreature;
			Buffs.applyBuffsToTarget(player, targetPlayer);
			return true;
		}
		
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return new String[]
		{
			COMMAND
		};
	}
	
}
