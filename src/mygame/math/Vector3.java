package mygame.math;

public class Vector3 {

  public int x;
  public int y;
  public int z;

  public Vector3() {
    this.x = 0;
    this.y = 0;
    this.z = 0;
  }

  public Vector3(Vector3 other) {
    this.x = other.x;
    this.y = other.y;
    this.z = other.z;
  }

  public Vector3(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3(int[] array) {
    if (array.length != 3) {
      throw new RuntimeException("Must create vector with 3 element array");
    }

    this.x = array[0];
    this.y = array[1];
    this.z = array[2];
  }

  public int[] array() {
    int[] ret = new int[3];
    ret[0] = x;
    ret[1] = y;
    ret[2] = z;
    return ret;
  }

  public Vector3 add(Vector3 rhs) {
    return new Vector3(
            x + rhs.x,
            y + rhs.y,
            z + rhs.z);
  }

  public Vector3 add(int x, int y, int z) {
    return new Vector3(
            this.x + x,
            this.y + y,
            this.z + z);
  }

  public Vector3 sub(Vector3 rhs) {
    return new Vector3(
            x - rhs.x,
            y - rhs.y,
            z - rhs.z);
  }

  public Vector3 neg() {
    return new Vector3(-x, -y, -z);
  }
  public static final Vector3 Null = new Vector3();

  public Vector3 mul(int c) {
    return new Vector3(c * x, c * y, c * z);
  }

  public int dot(Vector3 rhs) {
    return x * rhs.x
            + y * rhs.y
            + z * rhs.z;
  }

  public Vector3 cross(Vector3 rhs) {
    return new Vector3(
            y * rhs.z - z * rhs.y,
            x * rhs.z - z * rhs.x,
            x * rhs.y - y * rhs.x);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Vector3) {
      Vector3 rhs = (Vector3) obj;

      return x == rhs.x
              && y == rhs.y
              && z == rhs.z;
    } else {
      return false;
    }

  }

  @Override
  public int hashCode() {
    int hash = x;
    hash = hash * 127 + y;
    hash = hash * 131 + z;
    return hash;
  }

  public double norm() {
    return Math.sqrt(this.dot(this));
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public int z() {
    return z;
  }

  public String toString() {
    return "( " + x + ", " + y + ", " + z + " )";
  }

  public boolean isStrictSmallerThen(Vector3 other) {
    return other.x() > x() && other.y() > y() && other.z() > z();
  }

  public boolean isEven() {
    int sumRem = (x + y + z) % 2;
    return sumRem == 0 || sumRem == -2;
  }

  public boolean isEvenXZ() {
    int sumRem = (x + z) % 2;
    return sumRem == 0 || sumRem == -2;
  }

  public boolean isPositive() {
    return x() >= 0 && y() >= 0 && z() >= 0;
  }

  public int norm1() {
    return Math.abs(x() + Math.abs(y()) + Math.abs(z()));

  }

  public int norm1xz() {
    return Math.abs(x() + Math.abs(z()));

  }
}