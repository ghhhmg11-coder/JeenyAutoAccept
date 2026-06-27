package com.jeeny.autoaccept

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.SharedPreferences
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class JeenyAccessibilityService : AccessibilityService() {

    private val TAG = "JeenyAutoAccept"
    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        prefs = getSharedPreferences("jeeny_settings", MODE_PRIVATE)
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.notificationTimeout = 100
        info.packageNames = arrayOf("com.jeeny.driver", "com.careem.acma")
        serviceInfo = info
        Log.d(TAG, "خدمة القبول التلقائي تم تشغيلها")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val autoAcceptEnabled = prefs.getBoolean("auto_accept", false)
        if (!autoAcceptEnabled) return

        val rootNode: AccessibilityNodeInfo = rootInActiveWindow ?: return
        scanAndAcceptOrder(rootNode)
        rootNode.recycle()
    }

    private fun scanAndAcceptOrder(node: AccessibilityNodeInfo?) {
        if (node == null) return

        if (node.text != null) {
            val text = node.text.toString().trim()
            val acceptKeywords = listOf(
                "قبول", "قبول العرض", "Accept", "ACCEPT",
                "اقبل", "قبول الطلب", "Accept Ride", "Accept Order"
            )
            val isAcceptButton = acceptKeywords.any {
                text.equals(it, ignoreCase = true) || text.contains(it, ignoreCase = true)
            }

            if (isAcceptButton) {
                Log.d(TAG, "تم العثور على زر القبول: $text")
                if (node.isClickable) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.d(TAG, "تم القبول التلقائي بنجاح!")
                    return
                } else {
                    var parent = node.parent
                    while (parent != null) {
                        if (parent.isClickable) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            Log.d(TAG, "تم الضغط على الحاوية بنجاح!")
                            parent.recycle()
                            return
                        }
                        val nextParent = parent.parent
                        parent.recycle()
                        parent = nextParent
                    }
                }
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            scanAndAcceptOrder(child)
            child?.recycle()
        }
    }

    override fun onInterrupt() {
        Log.e(TAG, "تم إيقاف الخدمة")
    }
}
