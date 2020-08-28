package com.whywhom.soft.whyradiobox.event

import com.whywhom.soft.whyradiobox.model.Entry
import com.whywhom.soft.whyradiobox.model.SearchResult

open class FragmentEvent(var activityClass: Class<*>? = null, var entry:SearchResult ) : MessageEvent()