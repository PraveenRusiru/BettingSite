package org.exampl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.UpcomingMatchDTO;
import org.exampl.backend.entity.CricketMatch;
import org.exampl.backend.entity.MatchFormat;
import org.exampl.backend.service.MatchService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.exampl.backend.dto.ApiResponse;
import org.exampl.backend.dto.MatchDataDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class LiveMatchController {
    private final MatchService matchService;
    @GetMapping("/live")
    public ResponseEntity<ApiResponse> getLiveMatches() throws IOException {
        String url = "https://www.cricbuzz.com/cricket-match/live-scores";
//        String url="https://www.espncricinfo.com/live-cricket-score";

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.1 Safari/537.36")
                .get();



        Elements matches = doc.select(".cb-scr-wll-chvrn.cb-lv-scrs-col");



        List<MatchDataDTO> matchDataDTOS = new ArrayList<>();

        Pattern pattern = Pattern.compile("^([A-Za-z.]+)(?:\\s+(.+))?$");

        for (Element match : matches) {

            System.out.println("Match "+match.text());
            MatchDataDTO matchDataDTO = new MatchDataDTO();
            Matcher matcherBat = pattern.matcher(match.select("div.cb-hmscg-bat-txt ").text());
            String battingTeam="";
            String bowlingTeam="";
            if(matcherBat.find()){

                battingTeam = matcherBat.group(1).replaceAll("[^A-Za-z]", "");
                System.out.println("Batting Team "+battingTeam);
                System.out.println("Batting score "+matcherBat.group(2));
                matchDataDTO.setBattingTeam(matcherBat.group(1).replaceAll("[^A-Za-z]", ""));
                matchDataDTO.setBattingScore(matcherBat.group(2));
            }

            Matcher matchBowl=pattern.matcher(match.select("div.cb-hmscg-bwl-txt ").text());

                if(matchBowl.find()){
                    bowlingTeam = matchBowl.group(1).replaceAll("[^A-Za-z]", "");
                    System.out.println("Bowl team"+bowlingTeam);
                    System.out.println("Bowl score"+matchBowl.group(2));
                    matchDataDTO.setBowlingTeam(matchBowl.group(1).replaceAll("[^A-Za-z]", ""));
                    matchDataDTO.setBowlingScore(matchBowl.group(2));
                }
                String status=match.select("div.cb-text-live").text();
            matchDataDTO.setStatus(match.select("div.cb-text-live").text());
//div.w-full.py-2.px-3.bg-cbGrpHdrBkg.font-bold.capitalize.text-xs.flex.justify-between.items-center.text-cbTxtSec
            System.out.println("---------------------------\n battingTeam ="+battingTeam+" String bowlingTeam = "+bowlingTeam+" status "+status);
// stay commented               if(!bowlingTeam.equals("") && !battingTeam.equals("")){
                    System.out.println("----------------\n    habibi come to condition");
                    Map<String, String> matchType = getMatchType(battingTeam, bowlingTeam,status);
                    if(matchType.get("matchFormat")!=null){
                        switch (matchType.get("matchFormat")){
                            case "TEST"->matchDataDTO.setMatchFormat(MatchFormat.TEST);
                            case "T20"->matchDataDTO.setMatchFormat(MatchFormat.T20);
                            case "ODI" ->matchDataDTO.setMatchFormat(MatchFormat.ODI);
                        }
                    }

//                    matchDataDTO.setMatchFormat();
                    matchDataDTO.setBattingTeam(matchType.get("teamOne"));
                    matchDataDTO.setBowlingTeam(matchType.get("teamTwo"));
                    matchDataDTO.setTournament(matchType.get("seriesName"));
                    matchDataDTO.setVenue(matchType.get("venueInfo"));
//                }

            matchDataDTO.setTime("Live");
            System.out.println("Match name " + matchDataDTO.getTournament());
            System.out.println("Bat "+matchDataDTO.getBattingTeam());
            System.out.println("Bowl "+matchDataDTO.getBowlingTeam()+"\n\n=========================\n\n");
//            System.out.println("Live "+match.select("div.cb-text-live").text());
            matchDataDTOS.add(matchDataDTO);
        }
        if(matchDataDTOS.size()>0){
            for(MatchDataDTO matchDataDTO : matchDataDTOS){
                CricketMatch cricketMatch = matchService.existMatch(matchDataDTO);
                if(cricketMatch==null){
                    matchService.saveMatch(matchDataDTO);
                    System.out.println("Match "+matchDataDTO.getBattingTeam()+" "+matchDataDTO.getTournament()+" "+matchDataDTO.getTime());
                }else{
                    matchDataDTO.setId(cricketMatch.getMatchId());
                }
            }


            return ResponseEntity.ok(new ApiResponse(200,"OK",matchDataDTOS));
        }else{
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/upcoming")

    public ResponseEntity<ApiResponse> getUpcomingMatches() throws IOException {
        String url = "https://m.cricbuzz.com/cricket-match/live-scores/upcoming-matches";

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.1 Mobile Safari/537.36")
                .get();

        // Select all <script type="application/ld+json">
        Elements scripts = doc.select("script[type=application/ld+json]");
        List<UpcomingMatchDTO> upcomingMatchDTOS = new ArrayList<>();
        for (Element script : scripts) {
            String json = script.html().trim();

            JSONObject root = new JSONObject(json);

            // Check if this block is a WebPage with mainEntity
            if ("WebPage".equals(root.optString("@type"))) {
                JSONObject mainEntity = root.optJSONObject("mainEntity");

                if (mainEntity != null && "ItemList".equals(mainEntity.optString("@type"))) {
                    JSONArray items = mainEntity.optJSONArray("itemListElement");

                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);

                            if ("SportsEvent".equals(item.optString("@type"))) {
                                UpcomingMatchDTO upcomingMatchDTO = new UpcomingMatchDTO();
                                upcomingMatchDTO.setMatchId(String.valueOf(i));
                                upcomingMatchDTO.setMatch(item.getString("name"));
                                upcomingMatchDTO.setSuperEvent(item.getString("superEvent"));
                                upcomingMatchDTO.setLocation(item.getString("location"));
                                upcomingMatchDTO.setStartDate(item.getString("startDate"));
                                upcomingMatchDTO.setEventStatus(item.getString("eventStatus"));


//                                System.out.println("Match: " + item.getString("name"));
//                                System.out.println("Super Event: " + item.getString("superEvent"));
//                                System.out.println("Location: " + item.getString("location"));
//                                System.out.println("Start Date: " + item.getString("startDate"));
//                                System.out.println("Status: " + item.getString("eventStatus"));
                                JSONArray competitors = item.getJSONArray("competitor");
                                for (int j = 0; j < competitors.length(); j++) {
                                    JSONObject team = competitors.getJSONObject(j);
                                    if(j==0){upcomingMatchDTO.setTeamA(team.getString("name"));
                                    }else if(j==1){
                                        upcomingMatchDTO.setTeamB(team.getString("name"));
                                    }
//                                    System.out.println("Team: " + team.getString("name"));
                                }
//                                System.out.println("---------------");
                                upcomingMatchDTOS.add(upcomingMatchDTO);
                            }
                        }
                    }
                }
            }
        }
        if(upcomingMatchDTOS.size()>0){
            return ResponseEntity.ok(new ApiResponse(200,"OK",upcomingMatchDTOS));
        }else{
            return ResponseEntity.internalServerError().build();
        }

    }

//    @GetMapping("/matchType")
    public Map<String,String> getMatchType( String teamOne,String teamTwo,String status) throws IOException {
//        System.out.println("=======Entered to getMatchType===============");
        String url = "https://m.cricbuzz.com/cricket-match/live-scores";
//        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.1 Mobile Safari/537.36")
                    .get();

            Elements scriptTags = doc.select("script");
        String jsonData = null;

            for (Element script : scriptTags) {
                String scriptContent = script.html();
                // Look for the script that contains the match data pattern
                if (scriptContent.contains("self.__next_f.push") && scriptContent.contains("matchesList")) {
//                    System.out.println("script "+script.outerHtml());
                    int startIndex = scriptContent.indexOf("[1,\"7:");
//                    System.out.println("startIndex "+startIndex);
                    if (startIndex != -1) {
                        jsonData = scriptContent.substring(startIndex);
//                        System.out.println("jsonData "+jsonData);
                        break;
                    }
                }
            }

        if (jsonData != null) {
            JSONArray jsonArray = new JSONArray(jsonData);

            // The second element (index 1) is a string that contains the actual JSON data
            String matchesDataString = jsonArray.getString(1);

            String cleanJson = matchesDataString.substring(19);

            JSONObject mainObject = new JSONObject(cleanJson);

            JSONArray childrenArray = mainObject.getJSONArray("children");
            JSONArray body = childrenArray.getJSONArray(1);
            JSONObject bodyProps = body.getJSONObject(3);
            JSONArray bodyChildren = bodyProps.getJSONArray("children");

            // The div containing the data is the second child of body (index 1)
            JSONArray div = bodyChildren.getJSONArray(1);
            JSONObject divProps = div.getJSONObject(3);
            JSONArray divChildren = divProps.getJSONArray("children");

            // The $L10 component is at index 3 in div children
            JSONArray l10Component = divChildren.getJSONArray(3);
            JSONObject l10Props = l10Component.getJSONObject(3);

            // Finally, extract the matchesList
            JSONObject currentMatchesList = l10Props.getJSONObject("currentMatchesList");
//            System.out.println("=============MatchList "+currentMatchesList.toString());
            JSONArray typeMatches = currentMatchesList.getJSONArray("typeMatches");

//            System.out.println("Found " + typeMatches.length() + " matches:");

            // Find the match where team1 has teamSName "SZONE"
//            Map<String,String> matchDataMap=new HashMap<>();

            Map<String, String> matchDataMap = new HashMap<>();

            // Search through all matches
            for (int i = 0; i < typeMatches.length(); i++) {
                JSONObject typeMatch = typeMatches.getJSONObject(i);
                JSONArray seriesMatches = typeMatch.getJSONArray("seriesMatches");

                for (int j = 0; j < seriesMatches.length(); j++) {
                    JSONObject seriesMatch = seriesMatches.getJSONObject(j);
                    JSONObject seriesAdWrapper = seriesMatch.getJSONObject("seriesAdWrapper");
                    JSONArray seriesMatchesArray = seriesAdWrapper.getJSONArray("matches");
//                    System.out.println("matches "+seriesMatchesArray.toString());
                    for (int k = 0; k < seriesMatchesArray.length(); k++) {
                        JSONObject currentMatch = seriesMatchesArray.getJSONObject(k);
                        JSONObject currentMatchInfo = currentMatch.getJSONObject("matchInfo");
//                        System.out.println("currentMatchInfo "+currentMatchInfo.toString());
                        // Check both team1 and team2

                       if(!teamOne.equals("") || !teamTwo.equals("")){

                           JSONObject team1 = currentMatchInfo.getJSONObject("team1");
                           JSONObject team2 = currentMatchInfo.getJSONObject("team2");

                           String team1SName = team1.getString("teamSName");
                           String team2SName = team2.getString("teamSName");
                           String team1Name = team1.getString("teamName");
                           String team2Name = team2.getString("teamName");

                           // Check if either teamOne or teamTwo matches team1 or team2
                           if (team1SName.contains(teamOne) || teamOne.equals(team1Name) ||
                                   team2SName.contains(teamOne) || teamOne.equals(team2Name) &&
                                   team1SName.contains(teamTwo) || teamTwo.equals(team1Name) ||
                                   team2SName.contains(teamTwo) || teamTwo.equals(team2Name)) {

                               System.out.println("Found match with " + teamOne + " or " + teamTwo + ":");
                               System.out.println("Series: " + seriesAdWrapper.getString("seriesName"));
                               System.out.println("Match: " + currentMatchInfo.getString("matchDesc"));
                               System.out.println("Match Format: " + currentMatchInfo.getString("matchFormat"));
                               System.out.println("Team 1: " + team1Name + " (" + team1SName + ")");
                               System.out.println("Team 2: " + team2Name + " (" + team2SName + ")");
                               System.out.println("Status: " + currentMatchInfo.getString("status"));
                               System.out.println("----------------------------------------");

                               // Add to result map
                               matchDataMap.put("seriesName", seriesAdWrapper.getString("seriesName"));
                               matchDataMap.put("matchDesc", currentMatchInfo.getString("matchDesc"));
                               matchDataMap.put("matchFormat", currentMatchInfo.getString("matchFormat"));
                               matchDataMap.put("venueInfo", currentMatchInfo.getJSONObject("venueInfo").getString("city")+","+currentMatchInfo.getJSONObject("venueInfo").getString("ground"));
                               matchDataMap.put("teamOne", team2Name + " (" + team2SName + ")");
                               matchDataMap.put("teamTwo", team1Name + " (" + team1SName + ")");
//                            matchDataMap.put("status", currentMatchInfo.getString("status"));
                               return matchDataMap; // Return first match found
                           }
                       }else{
                           System.out.println("status "+status);
                           String matchStatus = currentMatchInfo.getString("status");
                           if(status.equals(matchStatus)){
                               JSONObject team1 = currentMatchInfo.getJSONObject("team1");
                               JSONObject team2 = currentMatchInfo.getJSONObject("team2");
                               String team1SName = team1.getString("teamSName");
                               String team2SName = team2.getString("teamSName");
                               String team1Name = team1.getString("teamName");
                               String team2Name = team2.getString("teamName");
                               System.out.println("Found match with " );
                               System.out.println("Series: " + seriesAdWrapper.getString("seriesName"));
                               System.out.println("Match: " + currentMatchInfo.getString("matchDesc"));
                               System.out.println("Match Format: " + currentMatchInfo.getString("matchFormat"));
                               System.out.println("Team 1: " + team1Name + " (" + team1SName + ")");
                               System.out.println("Team 2: " + team2Name + " (" + team2SName + ")");
                               System.out.println("Status: " + currentMatchInfo.getString("status"));
                               System.out.println("----------------------------------------");

                               // Add to result map
                               matchDataMap.put("seriesName", seriesAdWrapper.getString("seriesName"));
                               matchDataMap.put("matchDesc", currentMatchInfo.getString("matchDesc"));
                               matchDataMap.put("matchFormat", currentMatchInfo.getString("matchFormat"));
                               matchDataMap.put("venueInfo", currentMatchInfo.getJSONObject("venueInfo").getString("city")+","+currentMatchInfo.getJSONObject("venueInfo").getString("ground"));
                               matchDataMap.put("teamOne", team1Name + " (" + team1SName + ")");
                               matchDataMap.put("teamTwo", team2Name + " (" + team2SName + ")");
//                            matchDataMap.put("status", currentMatchInfo.getString("status"));
                               return matchDataMap; // Return first match found
                           }
                       }

                    }
                }
            }
//            for (int i = 0; i < typeMatches.length(); i++) {
//                JSONObject match = matches.getJSONObject(i);
//                JSONObject matchData = match.getJSONObject("match");
//                JSONObject matchInfo = matchData.getJSONObject("matchInfo");
//                JSONObject team1 = matchInfo.getJSONObject("team1");
//                JSONObject team2=matchInfo.getJSONObject("team2");
//                System.out.println("======WARNING !!!!! \n || team1 "+team1+"  ||\n || team2 "+team2+" || ");
//                if (team1.has("teamSName") && teamOne.equals(team1.getString("teamSName")) && team2.has("teamSName") && teamTwo.equals(team2.getString("teamSName"))) {
//                    System.out.println("Found match with SZONE as team1:");
//                    System.out.println(matchInfo.toString(4));
//                    System.out.println("seriesName "+matchInfo.getString("seriesName"));
//                    // Extract specific information
//                    matchDataMap.put("seriesName",matchInfo.getString("seriesName"));
//
//                    System.out.println("\nMatch Details:");
//
//                    System.out.println("matchFormat "+matchInfo.getString("matchFormat"));
//                    matchDataMap.put("matchFormat",matchInfo.getString("matchFormat"));
//
//                    JSONObject venueInfo = matchInfo.getJSONObject("venueInfo");
//
//                    matchDataMap.put("venueInfo",venueInfo.getString("city")+venueInfo.getString("ground"));
//                    System.out.println("city "+venueInfo.getString("city"));
//                    System.out.println("ground "+venueInfo.getString("ground"));
//
//                    System.out.println("Match: " + matchInfo.getString("matchDesc"));
//                    matchDataMap.put("matchType",matchInfo.getString("matchType"));
//
//                    if(team1.getString("teamSName").equals(teamOne)){
//                        matchDataMap.put("teamOne",team1.getString("teamName"));
//                    }else{
//                        matchDataMap.put("teamTwo",team2.getString("teamName"));
//                    }
//                    matchDataMap.put("team1Name",team1.getString("teamName"));
//                    System.out.println("Team 1: " + team1.getString("teamName") + " (" + team1.getString("teamSName") + ")");
//                    matchDataMap.put("team2Name",team2.getString("teamName"));
//                    System.out.println("Team 2: " + matchInfo.getJSONObject("team2").getString("teamName"));
//
//                    System.out.println("Status: " + matchInfo.getString("status")+"\n----------------------------");
//                    break;
//                }
//            }
            return matchDataMap;
        }
        return null;
    }
}
