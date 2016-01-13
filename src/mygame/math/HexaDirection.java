package mygame.math;
//Looking to the Z axis, X axis is left, top down.

public enum HexaDirection implements IHexaDirection {

  ZPlus {
    public boolean isUp() {
      return true;
    }

    public IHexaDirection goTwoClockWise() {
      return ZMinRight;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZMinLeft;
    }
  },
  ZPlusLeft {
    public boolean isUp() {
      return true;
    }

    public IHexaDirection goTwoClockWise() {
      return ZPlusRight;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZMin;
    }
  },
  ZPlusRight {
    public boolean isUp() {
      return true;
    }

    public IHexaDirection goTwoClockWise() {
      return ZMin;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZPlusLeft;
    }
  },
  ZMin {
    public boolean isUp() {
      return false;
    }

    public IHexaDirection goTwoClockWise() {
      return ZPlusRight;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZMin;
    }
  },
  ZMinLeft {
    public boolean isUp() {
      return false;
    }

    public IHexaDirection goTwoClockWise() {
      return ZPlus;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZMinRight;
    }
  },
  ZMinRight {
    public boolean isUp() {
      return false;
    }

    public IHexaDirection goTwoClockWise() {
      return ZMinLeft;
    }

    public IHexaDirection goTwoCounterClockWise() {
      return ZPlus;
    }
  }
}
