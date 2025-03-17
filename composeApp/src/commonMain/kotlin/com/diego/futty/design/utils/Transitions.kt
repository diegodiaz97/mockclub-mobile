package com.diego.futty.design.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

object Transitions {

    private const val TIME_DURATION = 300

    // Left screen
    val LeftScreenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val LeftScreenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    // Right screen

    val RightScreenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }

    val RightScreenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() ->
    ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        )
    }
}
