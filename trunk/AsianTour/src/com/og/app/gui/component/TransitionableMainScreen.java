package com.og.app.gui.component;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.MainScreen;

public class TransitionableMainScreen extends MainScreen {


    private boolean _isFading; 

    private int _fadeToColor;

    private int _currentAlpha;

    private int _timerId;

    private int _iFrame;

    private int _numFrames;


    private static int FRAMES_PER_SECOND = 15;




    protected void onExposed() {

        _isFading = false;

        super.onExposed();

    }


 

    protected void fadeToAndRun(int color, long d) {

        if (!_isFading) {

            _fadeToColor = color;


            _currentAlpha = 0;

            _isFading = true;

            _numFrames = (int) (d / 1000.0 * FRAMES_PER_SECOND);

            _iFrame = 0;

            nextFadeStep();

            startTimer();

        }

    }


 

    private void nextFadeStep() {

        if (_iFrame < _numFrames) {

            _iFrame += 1;

//            _currentAlpha = (int) (255 * Math.sqrt((float)_iFrame / _numFrames));
            _currentAlpha = 255 - (255/_numFrames) * _iFrame;

            invalidate();

        } else {
        	_currentAlpha=0;
            UiApplication.getUiApplication().cancelInvokeLater(_timerId);
            invalidate();
        }

    }




    private void startTimer() {

        _timerId = UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {

                TransitionableMainScreen.this.nextFadeStep();

            }

        }, 1000 / FRAMES_PER_SECOND, true);

    }




    protected void paint(Graphics graphics) {

        super.paint(graphics);

        if (_isFading) {

            graphics.setGlobalAlpha(_currentAlpha);

            graphics.setColor(_fadeToColor);

            XYRect rect = graphics.getClippingRect();

            graphics.fillRect(rect.x, rect.y, rect.width, rect.height);

        }

    }

}

