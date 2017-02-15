
#Smart Poll

## App store listing

link to Google Play listing: ```https://play.google.com/store/apps/details?id=com.smartiky.smartpoll```

## Description

Smart Poll gives the power of polling to you. Smart Poll is a platform for creating and answering questions. You can share polls publicly, to get opinions from all other Smart Poll users. Alternatively, you can share polls privately (only with select Facebook friends) or on your Facebook timeline.

Smart Poll currently has an application on Google Play and an application on Facebook. You can comment on polls and select your favorite polls and follow them over time. You can attach photos to your poll by taking a picture with you phone's camera, choosing a photo from your photo library or choosing an image from Google image search. Once other users comment or vote on your poll, you can receive notifications to see the responses and other activity on your poll.

## Key Features

- Facebook API integration

- See trends for votes

- Get push notification when polls receives response


## Key classes

link to key classes used in app: ```https://github.com/mozadeh/smartpoll/tree/master/bin/classes/com/smartiky/smartpoll```

## Class structure

```
/
├── AndroidManifest.xml
├── README.md
├── Readme
├── assets
│   ├── Roboto-Bold.ttf
│   ├── Roboto-BoldCondensed.ttf
│   ├── Roboto-BoldCondensedItalic.ttf
│   ├── Roboto-Condensed.ttf
│   ├── Roboto-CondensedItalic.ttf
│   ├── Roboto-Medium.ttf
│   └── Roboto-Regular.ttf
├── bin
│   ├── AndroidManifest.xml
│   ├── R.txt
│   ├── Smart\ Poll.apk
│   ├── classes
│   │   ├── com
│   │   │   ├── android
│   │   │   │   └── widget
│   │   │   │       ├── R$color.class
│   │   │   │       ├── R$drawable.class
│   │   │   │       ├── R$id.class
│   │   │   │       ├── R$layout.class
│   │   │   │       ├── R$string.class
│   │   │   │       └── R.class
│   │   │   ├── astuetz
│   │   │   │   └── viewpager
│   │   │   │       └── extensions
│   │   │   │           ├── R$attr.class
│   │   │   │           ├── R$color.class
│   │   │   │           ├── R$drawable.class
│   │   │   │           ├── R$styleable.class
│   │   │   │           └── R.class
│   │   │   ├── facebook
│   │   │   │   └── android
│   │   │   │       ├── R$attr.class
│   │   │   │       ├── R$color.class
│   │   │   │       ├── R$dimen.class
│   │   │   │       ├── R$drawable.class
│   │   │   │       ├── R$id.class
│   │   │   │       ├── R$layout.class
│   │   │   │       ├── R$string.class
│   │   │   │       ├── R$style.class
│   │   │   │       ├── R$styleable.class
│   │   │   │       └── R.class
│   │   │   ├── google
│   │   │   │   └── android
│   │   │   │       └── gms
│   │   │   │           ├── R$attr.class
│   │   │   │           ├── R$color.class
│   │   │   │           ├── R$drawable.class
│   │   │   │           ├── R$id.class
│   │   │   │           ├── R$string.class
│   │   │   │           ├── R$styleable.class
│   │   │   │           └── R.class
│   │   │   └── smartiky
│   │   │       └── smartpoll
│   │   │           ├── BuildConfig.class
│   │   │           ├── CommentListAdapter$1.class
│   │   │           ├── CommentListAdapter$ViewHolder.class
│   │   │           ├── CommentListAdapter.class
│   │   │           ├── FacebookActivity$1.class
│   │   │           ├── FacebookActivity$2.class
│   │   │           ├── FacebookActivity$3.class
│   │   │           ├── FacebookActivity$4.class
│   │   │           ├── FacebookActivity$5.class
│   │   │           ├── FacebookActivity$6.class
│   │   │           ├── FacebookActivity$myTrackedActivity.class
│   │   │           ├── FacebookActivity.class
│   │   │           ├── GCMBroadcastReceiver.class
│   │   │           ├── InternetSearchActivity$1.class
│   │   │           ├── InternetSearchActivity$2.class
│   │   │           ├── InternetSearchActivity$3.class
│   │   │           ├── InternetSearchActivity$4.class
│   │   │           ├── InternetSearchActivity.class
│   │   │           ├── LoginActivity$1.class
│   │   │           ├── LoginActivity.class
│   │   │           ├── Manifest$permission.class
│   │   │           ├── Manifest.class
│   │   │           ├── NewPollActivity$1.class
│   │   │           ├── NewPollActivity$2.class
│   │   │           ├── NewPollActivity$3.class
│   │   │           ├── NewPollActivity$4.class
│   │   │           ├── NewPollActivity$5.class
│   │   │           ├── NewPollActivity$6.class
│   │   │           ├── NewPollActivity$7.class
│   │   │           ├── NewPollActivity$8.class
│   │   │           ├── NewPollActivity$9.class
│   │   │           ├── NewPollActivity.class
│   │   │           ├── PollAnalyticsActivity$1.class
│   │   │           ├── PollAnalyticsActivity.class
│   │   │           ├── PollListAdapter.class
│   │   │           ├── PollResultsActivity$1$1.class
│   │   │           ├── PollResultsActivity$1.class
│   │   │           ├── PollResultsActivity$10.class
│   │   │           ├── PollResultsActivity$11.class
│   │   │           ├── PollResultsActivity$12.class
│   │   │           ├── PollResultsActivity$13.class
│   │   │           ├── PollResultsActivity$2.class
│   │   │           ├── PollResultsActivity$3.class
│   │   │           ├── PollResultsActivity$4.class
│   │   │           ├── PollResultsActivity$5.class
│   │   │           ├── PollResultsActivity$6$1$1.class
│   │   │           ├── PollResultsActivity$6$1.class
│   │   │           ├── PollResultsActivity$6$2.class
│   │   │           ├── PollResultsActivity$6.class
│   │   │           ├── PollResultsActivity$7$1.class
│   │   │           ├── PollResultsActivity$7.class
│   │   │           ├── PollResultsActivity$8$1.class
│   │   │           ├── PollResultsActivity$8.class
│   │   │           ├── PollResultsActivity$9.class
│   │   │           ├── PollResultsActivity.class
│   │   │           ├── PollViewHolder.class
│   │   │           ├── PollsActivity$SectionsPagerAdapter.class
│   │   │           ├── PollsActivity.class
│   │   │           ├── PollsFragment$1.class
│   │   │           ├── PollsFragment$2.class
│   │   │           ├── PollsFragment$3$1.class
│   │   │           ├── PollsFragment$3.class
│   │   │           ├── PollsFragment$4.class
│   │   │           ├── PollsFragment$5.class
│   │   │           ├── PollsFragment$6.class
│   │   │           ├── PollsFragment.class
│   │   │           ├── ProfileActivity$1.class
│   │   │           ├── ProfileActivity$2$1.class
│   │   │           ├── ProfileActivity$2.class
│   │   │           ├── ProfileActivity$3.class
│   │   │           ├── ProfileActivity$4.class
│   │   │           ├── ProfileActivity$SectionsPagerAdapter.class
│   │   │           ├── ProfileActivity$UsersFragment$1.class
│   │   │           ├── ProfileActivity$UsersFragment$2.class
│   │   │           ├── ProfileActivity$UsersFragment.class
│   │   │           ├── ProfileActivity.class
│   │   │           ├── R$anim.class
│   │   │           ├── R$attr.class
│   │   │           ├── R$bool.class
│   │   │           ├── R$color.class
│   │   │           ├── R$dimen.class
│   │   │           ├── R$drawable.class
│   │   │           ├── R$id.class
│   │   │           ├── R$layout.class
│   │   │           ├── R$string.class
│   │   │           ├── R$style.class
│   │   │           ├── R$styleable.class
│   │   │           ├── R.class
│   │   │           ├── SmartPollActivity$1.class
│   │   │           ├── SmartPollActivity$10.class
│   │   │           ├── SmartPollActivity$2.class
│   │   │           ├── SmartPollActivity$3.class
│   │   │           ├── SmartPollActivity$4$1.class
│   │   │           ├── SmartPollActivity$4.class
│   │   │           ├── SmartPollActivity$5.class
│   │   │           ├── SmartPollActivity$6$1.class
│   │   │           ├── SmartPollActivity$6.class
│   │   │           ├── SmartPollActivity$7.class
│   │   │           ├── SmartPollActivity$8.class
│   │   │           ├── SmartPollActivity$9.class
│   │   │           ├── SmartPollActivity.class
│   │   │           ├── UserListAdapter$ViewHolder.class
│   │   │           ├── UserListAdapter.class
│   │   │           ├── misc
│   │   │           │   ├── Constant.class
│   │   │           │   ├── GCMUtils$1.class
│   │   │           │   ├── GCMUtils$RegistrationIdListener.class
│   │   │           │   ├── GCMUtils.class
│   │   │           │   ├── ImageLoader$DrawableDisplayer.class
│   │   │           │   ├── ImageLoader$PhotoToLoad.class
│   │   │           │   ├── ImageLoader$PhotosLoader.class
│   │   │           │   ├── ImageLoader.class
│   │   │           │   ├── ImageTools.class
│   │   │           │   ├── Log.class
│   │   │           │   ├── Utils$1.class
│   │   │           │   ├── Utils$2.class
│   │   │           │   └── Utils.class
│   │   │           ├── system
│   │   │           │   ├── BackendConnection.class
│   │   │           │   ├── BackendResponse.class
│   │   │           │   ├── Choice.class
│   │   │           │   ├── Comment.class
│   │   │           │   ├── ImageSearch$ImageSearchResults.class
│   │   │           │   ├── ImageSearch.class
│   │   │           │   ├── NetworkObject.class
│   │   │           │   ├── Notifications.class
│   │   │           │   ├── Poll.class
│   │   │           │   ├── SmartPollSystem.class
│   │   │           │   └── User.class
│   │   │           └── widgets
│   │   │               ├── ChoiceButton$1.class
│   │   │               └── ChoiceButton.class
│   │   └── snippet
│   ├── classes.dex
│   ├── dexedLibs
│   │   ├── android-support-v4-0dc94cc5946bd8e2f5b8bec085003c15.jar
│   │   ├── annotations-3ad245d1646745441e4407c3546376b8.jar
│   │   ├── facebooksdk-bbec01be205c74f5a03f5ee18e508972.jar
│   │   ├── google-play-services-7957bec2fa2523cb6c249312f55b749f.jar
│   │   ├── google-play-services_lib-09460d2c78024ed825633ac26cbc5957.jar
│   │   ├── graphview-master-ed5ef3a1e265316886549f3e2dc065fd.jar
│   │   ├── libGoogleAnalyticsServices-153362376fa276cf8e49ca97764341f1.jar
│   │   ├── loadmorelistview-03398085e3c48a6268b1fb3bacfa27b2.jar
│   │   ├── org.apache.httpcomponents.httpclient_4.1.3-e5b157827828903a9a8bb40c8bb93403.jar
│   │   └── pagerslidingtabstrip-0175a2d09db4141bf8a203813c562b00.jar
│   ├── jarlist.cache
│   ├── res
│   │   └── crunch
│   │       ├── drawable
│   │       │   ├── b0001.png
│   │       │   ├── b0002.png
│   │       │   ├── b0003.png
│   │       │   ├── b0004.png
│   │       │   ├── b0005.png
│   │       │   ├── b0006.png
│   │       │   ├── b0007.png
│   │       │   ├── b0008.png
│   │       │   ├── b0009.png
│   │       │   ├── b0010.png
│   │       │   ├── b0011.png
│   │       │   ├── b0012.png
│   │       │   ├── b0013.png
│   │       │   ├── b0014.png
│   │       │   ├── b0015.png
│   │       │   ├── b0016.png
│   │       │   ├── b0017.png
│   │       │   ├── b0018.png
│   │       │   ├── b0019.png
│   │       │   ├── b0020.png
│   │       │   ├── b0021.png
│   │       │   ├── b0022.png
│   │       │   ├── b0023.png
│   │       │   ├── b0024.png
│   │       │   ├── b0025.png
│   │       │   ├── b0026.png
│   │       │   ├── b0027.png
│   │       │   ├── b0028.png
│   │       │   ├── b0029.png
│   │       │   ├── b0030.png
│   │       │   ├── b0031.png
│   │       │   ├── b0032.png
│   │       │   ├── b0033.png
│   │       │   ├── b0034.png
│   │       │   ├── b0035.png
│   │       │   ├── b0036.png
│   │       │   ├── b0037.png
│   │       │   ├── b0038.png
│   │       │   ├── b0039.png
│   │       │   ├── b0040.png
│   │       │   ├── b0041.png
│   │       │   ├── b0042.png
│   │       │   ├── b0043.png
│   │       │   ├── b0044.png
│   │       │   ├── b0045.png
│   │       │   ├── b0046.png
│   │       │   ├── b0047.png
│   │       │   ├── b0048.png
│   │       │   ├── b0049.png
│   │       │   ├── b0050.png
│   │       │   ├── b0051.png
│   │       │   ├── b0052.png
│   │       │   ├── b0053.png
│   │       │   ├── b0054.png
│   │       │   ├── b0055.png
│   │       │   ├── b0056.png
│   │       │   ├── b0057.png
│   │       │   ├── b0058.png
│   │       │   ├── b0059.png
│   │       │   ├── b0060.png
│   │       │   ├── b0061.png
│   │       │   ├── b0062.png
│   │       │   ├── b0063.png
│   │       │   ├── b0064.png
│   │       │   ├── bottom.png
│   │       │   ├── bottomleft.png
│   │       │   ├── bottomleft1.png
│   │       │   ├── bottomright.png
│   │       │   ├── bottomright1.png
│   │       │   ├── buttonexpandgradient1.png
│   │       │   ├── buttongradient.png
│   │       │   ├── buttongradient_selected.png
│   │       │   ├── buttongradientnotselected.png
│   │       │   ├── comment.png
│   │       │   ├── commentb.png
│   │       │   ├── facebook.png
│   │       │   ├── frame1.png
│   │       │   ├── frame10.png
│   │       │   ├── frame2.png
│   │       │   ├── frame2b.png
│   │       │   ├── frame3.png
│   │       │   ├── frame3b.png
│   │       │   ├── frame4.png
│   │       │   ├── frame4b.png
│   │       │   ├── frame5.png
│   │       │   ├── frame5b.png
│   │       │   ├── frame6.png
│   │       │   ├── frame6b.png
│   │       │   ├── frame7.png
│   │       │   ├── frame7b.png
│   │       │   ├── frame8.png
│   │       │   ├── frame8b.png
│   │       │   ├── frame9.png
│   │       │   ├── help.png
│   │       │   ├── ic_menu_add.png
│   │       │   ├── ic_menu_friendslist.png
│   │       │   ├── ic_menu_info_details.png
│   │       │   ├── ic_menu_invite.png
│   │       │   ├── ic_menu_refresh.png
│   │       │   ├── left.png
│   │       │   ├── love.png
│   │       │   ├── pixels2.png
│   │       │   ├── pixelsb.png
│   │       │   ├── pollbottom.png
│   │       │   ├── polltop.png
│   │       │   ├── powereby.png
│   │       │   ├── profilepic.png
│   │       │   ├── right.png
│   │       │   ├── tbackground.png
│   │       │   ├── top.png
│   │       │   ├── topleft.png
│   │       │   └── topright.png
│   │       ├── drawable-hdpi
│   │       │   ├── add.png
│   │       │   ├── addb.png
│   │       │   ├── addbs.png
│   │       │   ├── adviceb.png
│   │       │   ├── allpollsb.png
│   │       │   ├── bottomgradient.png
│   │       │   ├── bottomgradientselected.png
│   │       │   ├── businessb.png
│   │       │   ├── businessbs.png
│   │       │   ├── buttonexpandgradient.png
│   │       │   ├── buttonexpandgradient1.png
│   │       │   ├── buttongradient.png
│   │       │   ├── buttongradient_selected.png
│   │       │   ├── buttongradientnotenabled.png
│   │       │   ├── buttongradientnotselected.png
│   │       │   ├── comment.png
│   │       │   ├── createb.png
│   │       │   ├── createbs.png
│   │       │   ├── dropdownb.png
│   │       │   ├── expandbutton.png
│   │       │   ├── expandbuttons.png
│   │       │   ├── facebook.png
│   │       │   ├── fasionb.png
│   │       │   ├── fasionb1.png
│   │       │   ├── fasionbs.png
│   │       │   ├── favoriteb.png
│   │       │   ├── fewer.png
│   │       │   ├── following.png
│   │       │   ├── foodb.png
│   │       │   ├── foodbs.png
│   │       │   ├── gradient1.png
│   │       │   ├── gradient2.png
│   │       │   ├── gradient3.png
│   │       │   ├── gradient4.png
│   │       │   ├── gradient5.png
│   │       │   ├── ic_launcher.png
│   │       │   ├── ic_stat_notificationlogo.png
│   │       │   ├── image_loading.png
│   │       │   ├── leaderboardgradient.png
│   │       │   ├── localb.png
│   │       │   ├── localbs.png
│   │       │   ├── login.png
│   │       │   ├── logo.png
│   │       │   ├── love.png
│   │       │   ├── loveb.png
│   │       │   ├── more.png
│   │       │   ├── mypollsb.png
│   │       │   ├── mypollsb1.png
│   │       │   ├── newpollbottomselected.png
│   │       │   ├── newpollbutton.png
│   │       │   ├── no_image_available.png
│   │       │   ├── no_image_availableb.png
│   │       │   ├── otherb.png
│   │       │   ├── otherbs.png
│   │       │   ├── politics.png
│   │       │   ├── politicsb.png
│   │       │   ├── politicsbs.png
│   │       │   ├── pollbutton.png
│   │       │   ├── popularb.png
│   │       │   ├── profilepicsample.png
│   │       │   ├── radioactive.png
│   │       │   ├── radiohover.png
│   │       │   ├── radionormal.png
│   │       │   ├── recentb.png
│   │       │   ├── recentbs.png
│   │       │   ├── romanceb.png
│   │       │   ├── romancebs.png
│   │       │   ├── search.png
│   │       │   ├── searchb.png
│   │       │   ├── searchbs.png
│   │       │   ├── selectedcheckbox.png
│   │       │   ├── settings.png
│   │       │   ├── settingsb.png
│   │       │   ├── settingsbs.png
│   │       │   ├── shadow.png
│   │       │   ├── shadow1.png
│   │       │   ├── sourceb.png
│   │       │   ├── space.png
│   │       │   ├── sports.png
│   │       │   ├── sportsb.png
│   │       │   ├── sportsbs.png
│   │       │   ├── textfield1_selected.9.png
│   │       │   ├── textfield_default.9.png
│   │       │   ├── textfield_selected.9.png
│   │       │   ├── topgradient.png
│   │       │   ├── topgradientselected.png
│   │       │   ├── topmenugradient.png
│   │       │   ├── topmenugradientselected.png
│   │       │   ├── trendingb.png
│   │       │   ├── trendingbs.png
│   │       │   └── unselectedcheckbox.png
│   │       ├── drawable-hdpi-v11
│   │       │   └── ic_stat_notificationlogo.png
│   │       ├── drawable-ldpi
│   │       │   └── ic_launcher.png
│   │       ├── drawable-mdpi
│   │       │   ├── ic_launcher.png
│   │       │   └── ic_stat_notificationlogo.png
│   │       ├── drawable-mdpi-v11
│   │       │   └── ic_stat_notificationlogo.png
│   │       ├── drawable-xhdpi
│   │       │   ├── ic_launcher.png
│   │       │   └── ic_stat_notificationlogo.png
│   │       ├── drawable-xhdpi-v11
│   │       │   └── ic_stat_notificationlogo.png
│   │       ├── drawable-xxhdpi
│   │       │   └── ic_stat_notificationlogo.png
│   │       └── drawable-xxhdpi-v11
│   │           └── ic_stat_notificationlogo.png
│   └── resources.ap_
├── gen
│   └── com
│       ├── android
│       │   └── widget
│       │       └── R.java
│       ├── astuetz
│       │   └── viewpager
│       │       └── extensions
│       │           └── R.java
│       ├── facebook
│       │   └── android
│       │       └── R.java
│       ├── google
│       │   └── android
│       │       └── gms
│       │           └── R.java
│       └── smartiky
│           └── smartpoll
│               ├── BuildConfig.java
│               ├── Manifest.java
│               └── R.java
├── libs
│   ├── libGoogleAnalyticsServices.jar
│   └── org.apache.httpcomponents.httpclient_4.1.3.jar
├── lint.xml
├── project.properties
├── res
│   ├── anim
│   │   ├── fast_transition_left_to_right_out.xml
│   │   ├── list_layout_controller.xml
│   │   ├── right_slide_in.xml
│   │   ├── right_slide_out.xml
│   │   ├── scale.xml
│   │   ├── transition_left_to_right.xml
│   │   ├── transition_left_to_right_in.xml
│   │   ├── transition_left_to_right_out.xml
│   │   ├── transition_right_to_left.xml
│   │   └── transition_right_to_left_out.xml
│   ├── drawable
│   │   ├── anim_alphaenter.xml
│   │   ├── anim_alphaexit.xml
│   │   ├── anim_click.xml
│   │   ├── b0001.png
│   │   ├── b0002.png
│   │   ├── b0003.png
│   │   ├── b0004.png
│   │   ├── b0005.png
│   │   ├── b0006.png
│   │   ├── b0007.png
│   │   ├── b0008.png
│   │   ├── b0009.png
│   │   ├── b0010.png
│   │   ├── b0011.png
│   │   ├── b0012.png
│   │   ├── b0013.png
│   │   ├── b0014.png
│   │   ├── b0015.png
│   │   ├── b0016.png
│   │   ├── b0017.png
│   │   ├── b0018.png
│   │   ├── b0019.png
│   │   ├── b0020.png
│   │   ├── b0021.png
│   │   ├── b0022.png
│   │   ├── b0023.png
│   │   ├── b0024.png
│   │   ├── b0025.png
│   │   ├── b0026.png
│   │   ├── b0027.png
│   │   ├── b0028.png
│   │   ├── b0029.png
│   │   ├── b0030.png
│   │   ├── b0031.png
│   │   ├── b0032.png
│   │   ├── b0033.png
│   │   ├── b0034.png
│   │   ├── b0035.png
│   │   ├── b0036.png
│   │   ├── b0037.png
│   │   ├── b0038.png
│   │   ├── b0039.png
│   │   ├── b0040.png
│   │   ├── b0041.png
│   │   ├── b0042.png
│   │   ├── b0043.png
│   │   ├── b0044.png
│   │   ├── b0045.png
│   │   ├── b0046.png
│   │   ├── b0047.png
│   │   ├── b0048.png
│   │   ├── b0049.png
│   │   ├── b0050.png
│   │   ├── b0051.png
│   │   ├── b0052.png
│   │   ├── b0053.png
│   │   ├── b0054.png
│   │   ├── b0055.png
│   │   ├── b0056.png
│   │   ├── b0057.png
│   │   ├── b0058.png
│   │   ├── b0059.png
│   │   ├── b0060.png
│   │   ├── b0061.png
│   │   ├── b0062.png
│   │   ├── b0063.png
│   │   ├── b0064.png
│   │   ├── bottom.png
│   │   ├── bottom_bg_selector.xml
│   │   ├── bottomleft.png
│   │   ├── bottomleft1.png
│   │   ├── bottomright.png
│   │   ├── bottomright1.png
│   │   ├── buttonexpandgradient1.png
│   │   ├── buttongradient.png
│   │   ├── buttongradient_selected.png
│   │   ├── buttongradientnotselected.png
│   │   ├── comment.png
│   │   ├── commentb.png
│   │   ├── customdrawablecheckbox.xml
│   │   ├── edittext_modified_states.xml
│   │   ├── facebook.png
│   │   ├── favoritebutton.xml
│   │   ├── frame1.png
│   │   ├── frame10.png
│   │   ├── frame2.png
│   │   ├── frame2b.png
│   │   ├── frame3.png
│   │   ├── frame3b.png
│   │   ├── frame4.png
│   │   ├── frame4b.png
│   │   ├── frame5.png
│   │   ├── frame5b.png
│   │   ├── frame6.png
│   │   ├── frame6b.png
│   │   ├── frame7.png
│   │   ├── frame7b.png
│   │   ├── frame8.png
│   │   ├── frame8b.png
│   │   ├── frame9.png
│   │   ├── help.png
│   │   ├── ic_menu_add.png
│   │   ├── ic_menu_friendslist.png
│   │   ├── ic_menu_info_details.png
│   │   ├── ic_menu_invite.png
│   │   ├── ic_menu_refresh.png
│   │   ├── imagesourcebutton_text_selector.xml
│   │   ├── left.png
│   │   ├── love.png
│   │   ├── newbutton_bg_selector.xml
│   │   ├── newpollbottom_bg_selector.xml
│   │   ├── newpollbottom_text_selector.xml
│   │   ├── pixels2.png
│   │   ├── pixelsb.png
│   │   ├── pollbottom.png
│   │   ├── polltop.png
│   │   ├── powereby.png
│   │   ├── profilepic.png
│   │   ├── radio.xml
│   │   ├── right.png
│   │   ├── roundedgebutton.xml
│   │   ├── roundedgebuttonselected.xml
│   │   ├── roundedgebuttonselected1.xml
│   │   ├── roundedgebuttonselected2.xml
│   │   ├── roundedgebuttonselected3.xml
│   │   ├── roundedgebuttonselected4.xml
│   │   ├── roundedgebuttonselected5.xml
│   │   ├── searchbutton_bg_selector.xml
│   │   ├── selectedfavoritebutton.xml
│   │   ├── selectedsharebutton.xml
│   │   ├── settingsbutton_bg_selector.xml
│   │   ├── spin_animation.xml
│   │   ├── spin_animation_results.xml
│   │   ├── spin_animation_small.xml
│   │   ├── submitbottom_text_selector.xml
│   │   ├── submitcommentbutton.xml
│   │   ├── tab_bg_selector.xml
│   │   ├── tab_text_selector.xml
│   │   ├── tbackground.png
│   │   ├── top.png
│   │   ├── topleft.png
│   │   └── topright.png
│   ├── drawable-hdpi
│   │   ├── add.png
│   │   ├── addb.png
│   │   ├── addbs.png
│   │   ├── adviceb.png
│   │   ├── allpollsb.png
│   │   ├── bottomgradient.png
│   │   ├── bottomgradientselected.png
│   │   ├── businessb.png
│   │   ├── businessbs.png
│   │   ├── buttonexpandgradient.png
│   │   ├── buttonexpandgradient1.png
│   │   ├── buttongradient.png
│   │   ├── buttongradient_selected.png
│   │   ├── buttongradientnotenabled.png
│   │   ├── buttongradientnotselected.png
│   │   ├── comment.png
│   │   ├── createb.png
│   │   ├── createbs.png
│   │   ├── dropdownb.png
│   │   ├── expandbutton.png
│   │   ├── expandbuttons.png
│   │   ├── facebook.png
│   │   ├── fasionb.png
│   │   ├── fasionb1.png
│   │   ├── fasionbs.png
│   │   ├── favoriteb.png
│   │   ├── fewer.png
│   │   ├── following.png
│   │   ├── foodb.png
│   │   ├── foodbs.png
│   │   ├── gradient1.png
│   │   ├── gradient2.png
│   │   ├── gradient3.png
│   │   ├── gradient4.png
│   │   ├── gradient5.png
│   │   ├── ic_launcher.png
│   │   ├── ic_stat_notificationlogo.png
│   │   ├── image_loading.png
│   │   ├── leaderboardgradient.png
│   │   ├── localb.png
│   │   ├── localbs.png
│   │   ├── login.png
│   │   ├── logo.png
│   │   ├── love.png
│   │   ├── loveb.png
│   │   ├── more.png
│   │   ├── mypollsb.png
│   │   ├── mypollsb1.png
│   │   ├── newpollbottomselected.png
│   │   ├── newpollbutton.png
│   │   ├── no_image_available.png
│   │   ├── no_image_availableb.png
│   │   ├── otherb.png
│   │   ├── otherbs.png
│   │   ├── pagertitlestrip.xml
│   │   ├── politics.png
│   │   ├── politicsb.png
│   │   ├── politicsbs.png
│   │   ├── pollbutton.png
│   │   ├── popularb.png
│   │   ├── profilepicsample.png
│   │   ├── radioactive.png
│   │   ├── radiohover.png
│   │   ├── radionormal.png
│   │   ├── recentb.png
│   │   ├── recentbs.png
│   │   ├── romanceb.png
│   │   ├── romancebs.png
│   │   ├── search.png
│   │   ├── searchb.png
│   │   ├── searchbs.png
│   │   ├── selectedcheckbox.png
│   │   ├── settings.png
│   │   ├── settingsb.png
│   │   ├── settingsbs.png
│   │   ├── shadow.png
│   │   ├── shadow1.png
│   │   ├── sourceb.png
│   │   ├── space.png
│   │   ├── sports.png
│   │   ├── sportsb.png
│   │   ├── sportsbs.png
│   │   ├── textfield1_selected.9.png
│   │   ├── textfield_default.9.png
│   │   ├── textfield_selected.9.png
│   │   ├── topgradient.png
│   │   ├── topgradientselected.png
│   │   ├── topmenugradient.png
│   │   ├── topmenugradientselected.png
│   │   ├── trendingb.png
│   │   ├── trendingbs.png
│   │   └── unselectedcheckbox.png
│   ├── drawable-hdpi-v11
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-ldpi
│   │   └── ic_launcher.png
│   ├── drawable-mdpi
│   │   ├── ic_launcher.png
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-mdpi-v11
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-xhdpi
│   │   ├── ic_launcher.png
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-xhdpi-v11
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-xxhdpi
│   │   └── ic_stat_notificationlogo.png
│   ├── drawable-xxhdpi-v11
│   │   └── ic_stat_notificationlogo.png
│   ├── layout
│   │   ├── about.xml
│   │   ├── comment.xml
│   │   ├── follow_item.xml
│   │   ├── fragment_main_dummy.xml
│   │   ├── instructions.xml
│   │   ├── internetpicture.xml
│   │   ├── leaderboard_item.xml
│   │   ├── login.xml
│   │   ├── main.xml
│   │   ├── newpoll.xml
│   │   ├── pollanalytics.xml
│   │   ├── pollresults.xml
│   │   ├── polls_activity.xml
│   │   ├── polls_fragment.xml
│   │   ├── popmenu_item.xml
│   │   ├── popmenu_layout.xml
│   │   ├── question.xml
│   │   ├── questions_list.xml
│   │   ├── tabs_bg.xml
│   │   ├── testfragment.xml
│   │   ├── userprofile.xml
│   │   └── whatsnew.xml
│   └── values
│       ├── analytics.xml
│       └── strings.xml
└── src
    ├── com
    │   └── smartiky
    │       └── smartpoll
    │           ├── CommentListAdapter.java
    │           ├── FacebookActivity.java
    │           ├── GCMBroadcastReceiver.java
    │           ├── InternetSearchActivity.java
    │           ├── LoginActivity.java
    │           ├── NewPollActivity.java
    │           ├── PollAnalyticsActivity.java
    │           ├── PollListAdapter.java
    │           ├── PollResultsActivity.java
    │           ├── PollViewHolder.java
    │           ├── PollsActivity.java
    │           ├── PollsFragment.java
    │           ├── ProfileActivity.java
    │           ├── SmartPollActivity.java
    │           ├── UserListAdapter.java
    │           ├── misc
    │           │   ├── Constant.java
    │           │   ├── GCMUtils.java
    │           │   ├── ImageLoader.java
    │           │   ├── ImageTools.java
    │           │   ├── Log.java
    │           │   └── Utils.java
    │           ├── system
    │           │   ├── BackendConnection.java
    │           │   ├── BackendResponse.java
    │           │   ├── Choice.java
    │           │   ├── Comment.java
    │           │   ├── ImageSearch.java
    │           │   ├── NetworkObject.java
    │           │   ├── Notifications.java
    │           │   ├── Poll.java
    │           │   ├── SmartPollSystem.java
    │           │   └── User.java
    │           └── widgets
    │               └── ChoiceButton.java
    └── snippet
```

