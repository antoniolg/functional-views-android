/*
 * Copyright 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antonioleiva.functionalviewsandroid.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import com.antonioleiva.functionalviewsandroid.ui.common.Ui
import com.antonioleiva.functionalviewsandroid.ui.common.runUi

class FabAnimationBehaviour(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

    var isAnimatingOut = false
    var isAnimatingIn = false
    val interpolator = FastOutSlowInInterpolator()
    val duration = 200L

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
                                     directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton?,
                                target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                dyUnconsumed: Int) {
        when (child?.visibility) {
            View.VISIBLE -> {
                if (!isAnimatingOut && dyConsumed > 0) runUi(child.animateOut())
            }
            else -> if (!isAnimatingIn && dyConsumed < 0) runUi(child.animateIn())
        }
    }

    private fun FloatingActionButton?.animateIn() = Ui({
        this?.apply {
            visibility = View.VISIBLE
            animate()
                    .translationY(0f)
                    .setInterpolator(interpolator)
                    .setDuration(duration)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            isAnimatingIn = true
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            isAnimatingIn = false
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            isAnimatingIn = false
                        }
                    })
                    .start()
        }
    })

    private fun FloatingActionButton?.animateOut() = Ui({
        this?.apply {
            val y = (height + paddingBottom * 2).toFloat()
            animate()
                    .translationY(y)
                    .setInterpolator(interpolator)
                    .setDuration(duration)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            isAnimatingOut = true
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            isAnimatingOut = false
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            isAnimatingOut = false
                            visibility = View.GONE
                        }
                    })
                    .start()
        }
    })
}