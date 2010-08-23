package com.og.app.util;

public class DataCentre implements net.rim.device.api.util.Persistable {

	public DataCentre() {
	}

	// for TV Schedule
	public int tvIndex = 0;
	public String tvName = "";
	public String tvDate = "";
	public String tvBroadcastTime = "";
	public String tvBroadcaster = "";
	public String tvRegion = "";

	public DataCentre(int tvIndex, String tvName, String tvDate,
			String tvBroadcastTime, String tvBroadcaster, String tvRegion) {
		this.tvIndex = tvIndex;
		this.tvName = tvName;
		this.tvDate = tvDate;
		this.tvBroadcastTime = tvBroadcastTime;
		this.tvBroadcaster = tvBroadcaster;
		this.tvRegion = tvRegion;
	}

	// for Tour Schedule
	public int tourIndex = 0;
	public String tourDate = "";
	public String tourCountry = "";
	public String tourName = "";
	public String tourGClub = "";
	public String tourDefChampion = "";
	public String tourPrize = "";

	public DataCentre(int tourIndex, String tourDate, String tourCountry,
			String tourName, String tourGClub, String tourDefChampion,
			String tourPrize) {
		this.tourIndex = tourIndex;
		this.tourDate = tourDate;
		this.tourCountry = tourCountry;
		this.tourName = tourName;
		this.tourGClub = tourGClub;
		this.tourDefChampion = tourDefChampion;
		this.tourPrize = tourPrize;
	}

	// for Live Score
	// mark, country, pos, player, to par, hole, today, 1,2,3,4,total
	public int ls_index = 0;
	public String ls_mark = "";
	public String ls_country = "";
	public String ls_pos = "";
	public String ls_playerFirstName = "";
	public String ls_playerLastName = "";
	public String ls_toPar = "";
	public String ls_hole = "";
	public String ls_today = "";
	public String ls_r1 = "";
	public String ls_r2 = "";
	public String ls_r3 = "";
	public String ls_r4 = "";
	public String ls_total = "";

	public DataCentre(int ls_index, String ls_mark, String ls_country,
			String ls_pos, String ls_playerFirstName, String ls_playerLastName,
			String ls_toPar, String ls_hole, String ls_today, String ls_r1,
			String ls_r2, String ls_r3, String ls_r4, String ls_total) {
		this.ls_index = ls_index;
		this.ls_mark = ls_mark;
		this.ls_country = ls_country;
		this.ls_pos = ls_pos;
		this.ls_playerFirstName = ls_playerFirstName;
		this.ls_playerLastName = ls_playerLastName;
		this.ls_toPar = ls_toPar;
		this.ls_hole = ls_hole;
		this.ls_today = ls_today;
		this.ls_r1 = ls_r1;
		this.ls_r2 = ls_r2;
		this.ls_r3 = ls_r3;
		this.ls_r4 = ls_r4;
		this.ls_total = ls_total;
	}

	public int merit_index = 0;
	public String merit_player = "";
	public String merit_pos = "";
	public String merit_prize = "";
	public String merit_playerphotoURL = "";
	public String merit_played = "";
	public String temp1 = "";
	public String temp2 = "";
	public byte[] merit_playerphoto = null;

	//TODO-noted,ver:add temp1 and temp2 params to prevend duplicate method overriding
	public DataCentre(int merit_index, String merit_player, String merit_pos,
			String merit_prize, String merit_playerphotoURL, String merit_played, 
			String temp1, String temp2) {
		this.merit_index = merit_index;
		this.merit_player = merit_player;
		this.merit_pos = merit_pos;
		this.merit_prize = merit_prize;
		this.merit_playerphotoURL = merit_playerphotoURL;
		this.merit_played = merit_played;
		this.temp1 = temp1;
		this.temp2 = temp2;
	}

	public String country = "";

	public DataCentre(String country) {
		this.country = country;
	}

}
