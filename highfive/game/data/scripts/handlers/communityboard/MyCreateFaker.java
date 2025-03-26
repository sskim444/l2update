/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.communityboard;

import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.handler.CommunityBoardHandler;
import org.l2jmobius.gameserver.handler.IParseBoardHandler;
import org.l2jmobius.gameserver.model.actor.Player;

import sskim.faker.Faker;
import sskim.faker.FakerService;

/**
 * @author sskim
 */
public class MyCreateFaker implements IParseBoardHandler
{
	private static final String[] COMMANDS =
	{
		"_bbscreate"
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		return COMMANDS;
	}
	
	@Override
    public boolean parseCommunityBoardCommand(String command, Player player)
    {
        if (command.startsWith("_bbscreate"))
        {
        	String[] args = command.substring("_bbscreate;".length()).split(",");
            String job = args[0];
            String sex = args.length > 1 ? args[1] : "남"; // 기본값 설정

            FakerService.getInstance().createFaker(player, job, sex);
            
            return true;
        }

        return false;
    }
}
