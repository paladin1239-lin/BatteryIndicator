package com.pl_campus.battery_indicator.presentation


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl_campus.battery_indicator.R
import com.pl_campus.battery_indicator.presentation.ui.theme.BatteryIndicatorTheme
import androidx.constraintlayout.compose.ConstraintLayout
import com.pl_campus.battery_indicator.data.utils.HelpersImp
import com.pl_campus.battery_indicator.domain.utils.BatteryState
import com.pl_campus.battery_indicator.domain.utils.Helpers
import com.pl_campus.battery_indicator.domain.utils.Utils.BOTTOM_RANGE_HIGH
import com.pl_campus.battery_indicator.domain.utils.Utils.BOTTOM_RANGE_LOW
import com.pl_campus.battery_indicator.domain.utils.Utils.DURATION_MILLIS
import com.pl_campus.battery_indicator.domain.utils.Utils.SCALE_INITIAL_VALUE
import com.pl_campus.battery_indicator.domain.utils.Utils.SCALE_TARGET_VALUE
import com.pl_campus.battery_indicator.domain.utils.Utils.TOP_RANGE_HIGH
import com.pl_campus.battery_indicator.domain.utils.Utils.TOP_RANGE_LOW
import com.pl_campus.battery_indicator.presentation.ui.theme.SurfaceColor
import com.pl_campus.battery_indicator.presentation.ui.theme.heart_beat_max
import com.pl_campus.battery_indicator.presentation.ui.theme.heart_beat_min
import com.pl_campus.battery_indicator.presentation.ui.theme.heart_grey


@Composable
fun MainScreen(modifier: Modifier,
               helpers: Helpers? = null
){

    val context = LocalContext.current

    val percentBattery =  helpers?.getBatteryPercentage(context) ?: 0
    BatteryScreen(modifier, percentBattery, helpers)

}

@Composable
fun BatteryScreen(modifier: Modifier,
                  batteryPercent: Int,
                  helpers: Helpers? = null){
    val batteryState = when(batteryPercent){
        in BOTTOM_RANGE_LOW..TOP_RANGE_LOW -> BatteryState.LOW
        in BOTTOM_RANGE_HIGH..TOP_RANGE_HIGH -> BatteryState.HIGH
        else -> BatteryState.MEDIUM
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()
        .background(SurfaceColor)){
        val imageHeight = 60.dp
        val imagePadding = 20.dp
        val (left, center, right) = createRefs()

        val theScale = if(batteryState == BatteryState.LOW) {
            val infiniteTransition = rememberInfiniteTransition(label = "heartBeat")
            val scale: Float by infiniteTransition.animateFloat(
                initialValue = SCALE_INITIAL_VALUE,
                targetValue = SCALE_TARGET_VALUE,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = DURATION_MILLIS,
                        easing = FastOutSlowInEasing // Smooth easing
                    ),
                    repeatMode = RepeatMode.Reverse // Reverse the animation on each repetition
                ), label = "heartScale"
            )
            scale
        }
        else {
            SCALE_INITIAL_VALUE
        }


        val theColor = if(batteryState == BatteryState.LOW) {
            val color1 = heart_beat_max
            val color2 = heart_beat_min
            val ref = (SCALE_TARGET_VALUE - theScale)/(SCALE_TARGET_VALUE / SCALE_INITIAL_VALUE)
            val interpolatedColorCompose = helpers?.lerpColor(color1, color2, ref) ?: heart_beat_max
            interpolatedColorCompose
        }
        else {
            heart_grey
        }


        Box(
            modifier = modifier.
            background(SurfaceColor).constrainAs(center){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = R.drawable.battary),
                contentDescription = "Figure"
            )

            val battertImage = when(batteryState){
                BatteryState.LOW -> {
                    R.drawable.battery_low
                }
                BatteryState.HIGH -> R.drawable.battery_hight
                else -> R.drawable.battery_middle
            }
            Image(
                modifier = Modifier.align(Alignment.CenterStart),
                painter = painterResource(id = battertImage),
                contentDescription = "Figure"
            )
        }
        Box(modifier = Modifier.constrainAs(left){
            end.linkTo(center.start, margin = 16.dp)
            top.linkTo(center.top)
            bottom.linkTo(center.bottom)


        }.graphicsLayer {
            scaleX = theScale
            scaleY = theScale
        }.height(imageHeight).padding(top = (imageHeight/2), end = imagePadding),){
            Image(
                painter = if(batteryState == BatteryState.LOW) painterResource(id = R.drawable.heart_red) else painterResource(id = R.drawable.heart_grey),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = theColor)
            )
        }
        Box(modifier = Modifier.constrainAs(right){
            start.linkTo(center.end)
            top.linkTo(center.top)
            bottom.linkTo(center.bottom)
        }.height(imageHeight).padding(top = (imageHeight/2), start = imagePadding),){
            Image(
                painter = if(batteryState == BatteryState.HIGH) painterResource(id = R.drawable.leaf_green) else painterResource(id = R.drawable.leaf_gray),
                contentDescription = ""
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    BatteryIndicatorTheme {
        MainScreen(Modifier,
            HelpersImp())
    }
}