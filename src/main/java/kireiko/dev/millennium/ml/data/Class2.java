/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data;

import java.util.Arrays;
import java.util.List;
import lombok.Generated;

public class Class2 {
    public CheckResultML statisticsResult = new CheckResultML();

    public static class CheckResultML {
        public double UNUSUAL;
        public double STRANGE;
        public double SUSPECTED;
        public double SUSPICIOUSLY;

        public CheckResultML() {
            this.UNUSUAL = 0.0;
            this.STRANGE = 0.0;
            this.SUSPECTED = 0.0;
            this.SUSPICIOUSLY = 0.0;
        }

        public String toString() {
            return "[" + this.UNUSUAL + ", " + this.STRANGE + ", " + this.SUSPECTED + ", " + this.SUSPICIOUSLY + "]";
        }

        public List<Double> toList() {
            return Arrays.asList(this.UNUSUAL, this.STRANGE, this.SUSPECTED, this.SUSPICIOUSLY);
        }

        public void apply(List<Double> i, double m) {
            this.setUNUSUAL(this.getUNUSUAL() + i.get(0) / m);
            this.setSTRANGE(this.getSTRANGE() + i.get(1) / m);
            this.setSUSPECTED(this.getSUSPECTED() + i.get(2) / m);
            this.setSUSPICIOUSLY(this.getSUSPICIOUSLY() + i.get(3) / m);
        }

        @Generated
        public double getUNUSUAL() {
            return this.UNUSUAL;
        }

        @Generated
        public double getSTRANGE() {
            return this.STRANGE;
        }

        @Generated
        public double getSUSPECTED() {
            return this.SUSPECTED;
        }

        @Generated
        public double getSUSPICIOUSLY() {
            return this.SUSPICIOUSLY;
        }

        @Generated
        public void setUNUSUAL(double UNUSUAL) {
        }

        @Generated
        public void setSTRANGE(double STRANGE) {
        }

        @Generated
        public void setSUSPECTED(double SUSPECTED) {
        }

        @Generated
        public void setSUSPICIOUSLY(double SUSPICIOUSLY) {
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CheckResultML)) {
                return false;
            }
            CheckResultML other = (CheckResultML)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (Double.compare(this.getUNUSUAL(), other.getUNUSUAL()) != 0) {
                return false;
            }
            if (Double.compare(this.getSTRANGE(), other.getSTRANGE()) != 0) {
                return false;
            }
            if (Double.compare(this.getSUSPECTED(), other.getSUSPECTED()) != 0) {
                return false;
            }
            return Double.compare(this.getSUSPICIOUSLY(), other.getSUSPICIOUSLY()) == 0;
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof CheckResultML;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            long $UNUSUAL = Double.(double)ToLongBits(this.getUNUSUAL());
            result = result * 59 + (int)($UNUSUAL >>> 32 ^ $UNUSUAL);
            long $STRANGE = Double.(double)ToLongBits(this.getSTRANGE());
            result = result * 59 + (int)($STRANGE >>> 32 ^ $STRANGE);
            long $SUSPECTED = Double.(double)ToLongBits(this.getSUSPECTED());
            result = result * 59 + (int)($SUSPECTED >>> 32 ^ $SUSPECTED);
            long $SUSPICIOUSLY = Double.(double)ToLongBits(this.getSUSPICIOUSLY());
            result = result * 59 + (int)($SUSPICIOUSLY >>> 32 ^ $SUSPICIOUSLY);
            return result;
        }

        @Generated
        public CheckResultML(double UNUSUAL, double STRANGE, double SUSPECTED, double SUSPICIOUSLY) {
        }
    }
}

