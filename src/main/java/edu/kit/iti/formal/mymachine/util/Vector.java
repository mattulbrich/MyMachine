package edu.kit.iti.formal.mymachine.util;

import java.awt.*;
import java.util.Objects;

public class Vector {
    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector minus(Point a, Point b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public Vector normalize() {
        double l = length();
        if(l > 0.0) {
            return new Vector(x/l, y/l);
        } else {
            return this;
        }
    }

    public double length() {
        return Math.hypot(x, y);
    }

    public double dotProd(Vector v) {
        return x*v.x + y*v.y;
    }

    public Vector normal() {
        return new Vector(-y, x);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
