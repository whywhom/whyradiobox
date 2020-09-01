package com.whywhom.soft.whyradiobox.event

import com.whywhom.soft.whyradiobox.model.PodcastSearchResult

open class FragmentEvent(var activityClass: Class<*>? = null, var entry:PodcastSearchResult ) : MessageEvent()