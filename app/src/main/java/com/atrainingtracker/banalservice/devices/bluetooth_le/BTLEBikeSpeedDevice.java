/*
 * aTrainingTracker (ANT+ BTLE)
 * Copyright (C) 2011 - 2019 Rainer Blind <rainer.blind@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0
 */

package com.atrainingtracker.banalservice.devices.bluetooth_le;

import android.content.Context;
import android.util.Log;

import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.banalservice.devices.DeviceType;
import com.atrainingtracker.banalservice.sensor.MySensor;
import com.atrainingtracker.banalservice.sensor.MySensorManager;
import com.atrainingtracker.banalservice.sensor.SensorType;

public class BTLEBikeSpeedDevice extends BTLEBikeDevice {
    private static final String TAG = "BTLEBikeSpeedDevice";
    private static final boolean DEBUG = BANALService.DEBUG & false;
    private MySensor<Double> mPowerSensor;

    /**
     * constructor
     **/
    public BTLEBikeSpeedDevice(Context context, MySensorManager mySensorManager, long deviceID, String address) {
        super(context, mySensorManager, DeviceType.BIKE_SPEED, deviceID, address);
        if (DEBUG) Log.i(TAG, "created device");
    }

    @Override
    protected void addSensors() {
        if (DEBUG) Log.i(TAG, "addSensors()");

        addSpeedAndDistanceSensors();
        addCadenceSensor();
        mPowerSensor = new MySensor<Double>(this, SensorType.POWER);
        addSensor(mPowerSensor);
        mSpeedSensor.addSensorListener(new MySensor.SensorListener<Double>() {
            @Override
            public void newValue(Double speed) {
                double r = speed *  5.843;
                mCadenceSensor.newValue(r);
                double power = ((0.0011 * r + 0.0053) * r + 0.5157) * r;
                mPowerSensor.newValue(power);
            }
        });
    }

}
