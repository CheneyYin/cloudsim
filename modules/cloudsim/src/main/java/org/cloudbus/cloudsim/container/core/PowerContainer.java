package org.cloudbus.cloudsim.container.core;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.PowerGuestEntity;
import org.cloudbus.cloudsim.util.MathUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sareh on 23/07/15.
 */
public class PowerContainer extends Container implements PowerGuestEntity {
        /** The utilization history. */
        private final List<Double> utilizationHistory = new LinkedList<>();


        /** The previous time. */
        private double previousTime;


        /**
         * Instantiates a new power vm.
         *
         * @param id the id
         * @param userId the user id
         * @param mips the mips
         * @param pesNumber the pes number
         * @param ram the ram
         * @param bw the bw
         * @param size the size
         * @param vmm the vmm
         * @param cloudletScheduler the cloudlet scheduler
         * @param schedulingInterval the scheduling interval
         */
        public PowerContainer(
                final int id,
                final int userId,
                final double mips,
                final int pesNumber,
                final int ram,
                final long bw,
                final long size,
                final String vmm,
                final CloudletScheduler cloudletScheduler,
                final double schedulingInterval) {
            super(id, userId, mips, pesNumber, ram, bw, size, vmm, cloudletScheduler, schedulingInterval);
        }

        /**
         * Updates the processing of guest entities running on this VM.
         *
         * @param currentTime current simulation time
         * @param mipsShare array with MIPS share of each Pe available to the scheduler
         *
         * @return time predicted completion time of the earliest finishing cloudlet, or 0 if there is
         * 		no next events
         *
         * @pre currentTime >= 0
         * @post $none
         */
        @Override
        public double updateCloudletsProcessing(final double currentTime, final List<Double> mipsShare) {
            double time = super.updateCloudletsProcessing(currentTime, mipsShare);
            if (currentTime > getPreviousTime() && (currentTime - 0.2) % getSchedulingInterval() == 0) {
                double utilization = getTotalUtilizationOfCpu(getCloudletScheduler().getPreviousTime());
                if (CloudSim.clock() != 0 || utilization != 0) {
                    addUtilizationHistoryValue(utilization);
                }
                setPreviousTime(currentTime);
            }
            return time;
        }

        /**
         * Gets the utilization history.
         *
         * @return the utilization history
         */
        public List<Double> getUtilizationHistory() {
            return utilizationHistory;
        }

        /**
         * Gets the previous time.
         *
         * @return the previous time
         */
        public double getPreviousTime() {
            return previousTime;
        }

        /**
         * Sets the previous time.
         *
         * @param previousTime the new previous time
         */
        public void setPreviousTime(final double previousTime) {
            this.previousTime = previousTime;
        }
    }