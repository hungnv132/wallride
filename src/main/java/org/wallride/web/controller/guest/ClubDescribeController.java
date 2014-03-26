package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.Club;
import org.wallride.core.domain.League;
import org.wallride.core.service.ClubService;
import org.wallride.core.service.LeagueService;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("{language}/club/{leagueId}/{clubId}")
public class ClubDescribeController {

	@Inject
	private LeagueService leagueService;

	@Inject
	private ClubService clubService;

	@RequestMapping
	public String describe(
			@PathVariable Integer leagueId,
			@PathVariable Integer clubId,
			HttpSession session,
			Model model) {
		League league =leagueService.readLeagueById(leagueId);
		Club club =clubService.readClubById(clubId);
		model.addAttribute("league", league);
		model.addAttribute("club", club);
		return "/club";
	}
}
