package com.og.app.gui;

import java.io.IOException;

import com.og.app.gui.listener.*;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.rss.*;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;

public class NewsListField extends CustomListField {
        private NewsPanel newsPanel = null;
        private String feedname = "";
        private int newsID = 0;
        ANewsItemObj[] item = new ANewsItemObj[5];

        public NewsListField(NewsPanel newsPanel, ListFieldListener listener) {
                super(listener);
                this.newsPanel = newsPanel;
        }

        protected void onFocus(int direction) {
                newsPanel.invalidate();
        }

        protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {

                if ( dy<0 ){
                        int tmpy = dy*-1;
                        if ( getSelectedIndex()-tmpy<0 ){
                                if ( getSelectedIndex()>0 ){
                                        do{
                                                setSelectedIndex(getSelectedIndex()-1);
                                                tmpy--;
                                        }while(tmpy>0 );
                                }
                                listener.onListFieldUnfocus();
                                newsPanel.invalidate();        
                                return true;         
                        }
                }     
                newsPanel.invalidate();
                return false;
        }

        public boolean navigationClick(int status, int time) {
        	System.out.println("Clicked at news item");
                if(MenuScreen.getInstance().newsCollection.size() > 0)
                {
                        try{
                                synchronized(Application.getEventLock() ){
                                        ANewsItemObj ni = (ANewsItemObj)MenuScreen.getInstance().newsCollection.elementAt(getSelectedIndex());
                                        Screen s = UiApplication.getUiApplication().getActiveScreen();
                                        ni.index=getSelectedIndex();
                                        UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(this, ni));
                                }
                                return true;
                        }catch(Exception e){
                                System.out.println(e);
                        }              
                }
                return false;
        }

        public void loadNews(int newsID) {
                setRowHeight();


                synchronized(lock) {
                        this.newsID = newsID;

                        //removeAll();
                        for (int i = 0; i < 2; i++) {
                                System.out.println("aloy.NewsListField.loadNews: " + i);
                                //item[i] = new ANewsItemObj("Test news "+i, "Aloysius Ong", " really really long string that is more than 11 character 5mins ago", "Some very long description or news content that must store more than 50 characters in order to try and let it show the triple dots which we have created earlier. So folks, this is news " + i + ".", "1");
                                
                                if (i == 0) {
                                    item[i] = new ANewsItemObj("FRUSTRATING START FOR NOH", "Aloysius Ong", "15 July 2010", "Rising Korean star Noh Seung-yul was kicking himself after an even par 72 in perfect scoring conditions in the first round of the British Open on Thursday. \n\nPlaying alongside early pacesetters John Daly and Andrew Coltart, who fired matching 66s, the 19-year-old Noh held his own by moving to two-under through 15 at the Old Course.\n\nThe current Asian Tour Order of Merit leader then ran up a costly double bogey after an errant three iron tee shot on the 16th flew out of bounds by four yards. He parred the demanding 17th \"Road Hole\" and agonisingly missed a four-foot birdie chance on the closing hole.\n\n\"I\'m not happy. I made a few mistakes. The weather was so nice and the pins were not tough. I hit a lot of good tee shots but didn\'t hit too many good second shots and missed a few putts,\" said Noh, who is making his Open debut.\n\n\"On 16, I just hit a poor tee shot to the right which was disappointing as I was playing it safe from off the tee. I\'ve got to get to the range to work on my short game and iron play for tomorrow.\"\n\nAlthough the talented teenager failed to match 1995 Open champion Daly and crowd favourite Coltart, Noh said he learned a few tricks from his playing partners.", "1");
                                } else if (i == 1) {
                                    item[i] = new ANewsItemObj("CHIA ENJOYS FUN 69", "Aloysius Ong", "15 July 2010", " Malaysia’s Danny Chia gave himself a nine out of 10 after shooting an impressive three-under-par 69 in tough conditions at the British Open on Thursday.\n\nThe Asian Tour regular stormed to an outward 32 in his opening round at St Andrews before battling bravely when the weather turned dismal to trade two birdies with three bogeys.\n\nIt is the first time Chia has broken par in his third appearance at the world’s oldest Major and enhanced his hopes of becoming the first Malaysian to qualify for the weekend rounds.\n\n“It was good. I hit a lot of good shots as the conditions were tough in the afternoon. I’ll take this 69 anytime. On the back nine, it started to rain and the winds were blowing quite strongly. I tried to play a lot of conservative shots and hit a lot of great shots. It was hard to expect a good score on the back nine,” said Chia, who ended the day in tied 30th place, six shots behind leader Rory McIlroy.", "1");
                                } else if (i == 2) {
                                    item[i] = new ANewsItemObj("AKHMAL EAGER FOR SUCCESS", "Aloysius Ong", "14 July 2010", "Malaysia’s Akhmal Tarmizee is banking on home course knowledge to spur him to victory at the Negeri Sembilan Masters Invitational which starts on Thursday.\n\nAkhmal, 20, is steadying himself as he prepares to play against defending champion Lam Chih Bing of Singapore, Unho Park of Australia and Aung Win of Myanmar, currently leading the Asian Development Tour Order of Merit at the Seremban International Golf Club, located about an hour’s drive from Kuala Lumpur.\n\nIain Steel, Shaaban Hussin and Ben Leong, a winner on the Asian Tour alongside Nicholas Fung and Haziq Hamizan will spearhead the local charge at the RM500,000 (approximately US$156,000) tournament which is the first Malaysian event to join the Asian Development Tour this year.\n\nAkhmal is hoping to mend the heartbreak of missing his Asian Tour card by one stroke at the Qualifying School earlier this year.", "1");
                                } else if (i == 3) {
                                    item[i] = new ANewsItemObj("SUPER SIVA TAKES CHARGE", "Aloysius Ong", "14 July 2010", "S. Sivachandran, touted as one of the local players to challenge for the title at the RM500,000 (approximately US$156,000) Negeri Sembilan Masters Invitational 2010 this week, did not disappoint as he seized the first round lead at the 6,303-metre, par-72 Seremban International Golf Club (SIGC).\n\nBuoyed by a superb driving performance and a sizzling hot putter, Siva, 31, drained in three birdies at the third, fourth and sixth holes on the front nine before adding three more birdies at holes 11, 14 and 16 for a flawless round of six-under-par 66 for a two-shot advantage over local lad Akhmal Tarmizee Mohd Nazari and Filipino Mars Pucay.\n\n“I’m happy with my game. I made some long putts and generally avoided trouble. The golf course is not easy. It demands prudent shotmaking off the tee to get in good position,” said Siva who made a couple of marvelous 20-footers for birdies early in his round to get his challenge going.\n\n“This is my best start to the championship but I’m not getting ahead of myself here as there are three more rounds to go. I hope to do well and tomorrow, I will just go out and play,” added Siva who has a good record in the championship where he had a top ten finish last year and a sixth place finish the year before.", "1");
                                } else if (i == 4) {
                                    item[i] = new ANewsItemObj("THONGCHAI EYES MAJOR STRIPES", "Aloysius Ong", "13 July 2010", " Thai ace Thongchai Jaidee believes he is capable of winning a Major championship as he prepares a title assault at the British Open starting on Thursday.\n\nThe reigning Asian Tour number one said he previously aimed to simply make the halfway cut but after coming close at last year’s Open at Turnberry, he reckons his lifelong dream of hoisting a Major trophy can become a reality.\n\n“I am getting closer towards winning a Major. I used to set a goal of just making the cut as the motivation and last year, I did much better than the goal I set. This week I will try my best but I will not to put too much pressure on myself. I have a strong passion to win in my mind but we will see how it goes,” said Thongchai.\n\nThe former paratrooper entered last year’s final round four shots off the lead before eventually finishing tied 13th for his best result in a Major. Returning to the Home of Golf has also brought back happy memories as he wrote a small slice of history here in 2005 by becoming the first Thai to play in all four rounds, finishing tied 52nd.", "1");
                                }
                                
                                if(i==1){
                                        item[i].thumbnailurl = "http://www.bhare.org/golf.gif";
                                        item[i].imageurl = "http://radio.virtualdj.com/forum/Games/GolfMaster3DSte/GolfMaster3DSte.gif";
                                }
                                if(item[i].thumbnailurl.length()>0){
                                        //fetch bytes
                                        final int index = i;
                                        try {
                                                Utility.getWebData(item[i].thumbnailurl, new WebDataCallback() {
                                                        public void callback(String data) {
                                                                item[index].thumbnail = data.getBytes();                                                                
                                                        }
                                                });
                                        } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                                }
                                add(item[i]);

                        }
                }
        }

}
