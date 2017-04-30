
#Smart Poll

![alt tag](https://raw.githubusercontent.com/mozadeh/smartpoll/master/new_logo.jpg)

## App store listing

link to Google Play listing: [https://play.google.com/store/apps/details?id=com.smartiky.smartpoll](https://play.google.com/store/apps/details?id=com.smartiky.smartpoll)

## Team

- Mohammad Ghasemzadeh

- Hossein Kaffash Bokharei

## Description

Smart Poll gives the power of polling to you. Smart Poll is a platform for creating and answering questions. You can share polls publicly, to get opinions from all other Smart Poll users. Alternatively, you can share polls privately (only with select Facebook friends) or on your Facebook timeline.

Smart Poll currently has an application on Google Play and an application on Facebook. You can comment on polls and select your favorite polls and follow them over time. You can attach photos to your poll by taking a picture with you phone's camera, choosing a photo from your photo library or choosing an image from Google image search. Once other users comment or vote on your poll, you can receive notifications to see the responses and other activity on your poll.

## Libraries / Tutorials / APIs used

- Facebook Android SDK [https://developers.facebook.com/docs/android/](https://developers.facebook.com/docs/android/)

- Google Analytics Android SDK [https://developers.google.com/analytics/devguides/collection/android/v4/](https://developers.google.com/analytics/devguides/collection/android/v4/)

- Android Pull to Refresh Loadmore listview [https://github.com/incube8/android-pullToRefresh-loadMore](https://github.com/incube8/android-pullToRefresh-loadMore)

- Android Graphview [http://www.android-graphview.org/](http://www.android-graphview.org/)

- Pager Sliding Tab Strip [https://github.com/astuetz/PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)

- Google Image Search API [https://developers.google.com/image-search/](https://developers.google.com/image-search/)


## Key Features

- Facebook API integration

- See trends for votes

- Get push notification when polls receives response


## Key classes

link to key classes used in app: [https://github.com/mozadeh/smartpoll/tree/master/bin/classes/com/smartiky/smartpoll](https://github.com/mozadeh/smartpoll/tree/master/bin/classes/com/smartiky/smartpoll)

## File structure

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
│   │   │           ├── FacebookActivity$myTrackedActivity.class
│   │   │           ├── FacebookActivity.class
│   │   │           ├── GCMBroadcastReceiver.class
│   │   │           ├── InternetSearchActivity$1.class
│   │   │           ├── InternetSearchActivity$2.class
│   │   │           ├── InternetSearchActivity$3.class
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
│   │   │           ├── NewPollActivity.class
│   │   │           ├── PollAnalyticsActivity$1.class
│   │   │           ├── PollAnalyticsActivity.class
│   │   │           ├── PollListAdapter.class
│   │   │           ├── PollResultsActivity$1$1.class
│   │   │           ├── PollResultsActivity$1.class
│   │   │           ├── PollResultsActivity$10.class
│   │   │           ├── PollResultsActivity$11.class
│   │   │           ├── PollResultsActivity$2.class
│   │   │           ├── PollResultsActivity$3.class
│   │   │           ├── PollResultsActivity$4.class
│   │   │           ├── PollResultsActivity$5.class
│   │   │           ├── PollResultsActivity$6.class
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
│   │       │   ├── ...
│   │       │   ├── topleft.png
│   │       │   └── topright.png
│   │       ├── drawable-hdpi
│   │       │   ├── add.png
│   │       │   ├── ...
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
│   │   ├── ...
│   │   ├── topleft.png
│   │   └── topright.png
│   ├── drawable-hdpi
│   │   ├── add.png
│   │   ├── addb.png
│   │   ├── addbs.png
│   │   ├──...
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

