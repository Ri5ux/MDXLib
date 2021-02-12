package com.asx.mdx.lib.client.util;

public class Matrix3
{
    public static Matrix3[] rotations = { Matrix3.rotY(0), Matrix3.rotY(90), Matrix3.rotY(180), Matrix3.rotY(270) };

    public double m[][] = new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }
    };

    public static Matrix3 rot(double deg, int i, int j)
    {
        Matrix3 matrix = new Matrix3();
        double a = Math.toRadians(deg);
        double s = Math.sin(a);
        double c = Math.cos(a);
        matrix.m[i][i] = c;
        matrix.m[i][j] = -s;
        matrix.m[j][i] = s;
        matrix.m[j][j] = c;
        return matrix;
    }

    public static Matrix3 rotY(double degrees)
    {
        return rot(degrees, 2, 0);
    }

    public Matrix3 mul(Matrix3 mul)
    {
        Matrix3 matrix3 = new Matrix3();
        matrix3.m = new double[][] { { this.m[0][0] * mul.m[0][0] + this.m[0][1] * mul.m[1][0] + this.m[0][2] * mul.m[2][0], this.m[0][0] * mul.m[0][1] + this.m[0][1] * mul.m[1][1] + this.m[0][2] * mul.m[2][1], this.m[0][0] * mul.m[0][2] + this.m[0][1] * mul.m[1][2] + this.m[0][2] * mul.m[2][2] }, { this.m[1][0] * mul.m[0][0] + this.m[1][1] * mul.m[0][1] + this.m[1][2] * mul.m[2][1], this.m[1][0] * mul.m[0][1] + this.m[1][1] * mul.m[1][1] + this.m[1][2] * mul.m[2][1], this.m[1][0] * mul.m[0][2] + this.m[1][1] * mul.m[1][2] + this.m[1][2] * mul.m[2][2] }, { this.m[2][0] * mul.m[0][0] + this.m[2][1] * mul.m[1][0] + this.m[2][2] * mul.m[2][0], this.m[2][0] * mul.m[0][1] + this.m[2][1] * mul.m[1][1] + this.m[2][2] * mul.m[2][1], this.m[2][0] * mul.m[0][2] + this.m[2][1] * mul.m[1][2] + this.m[2][2] * mul.m[2][2] }
        };

        return matrix3;
    }

    public Vertex mul(double x, double y, double z)
    {
        return new Vertex(x * m[0][0] + y * m[1][0] + z * m[2][0], x * m[0][1] + y * m[1][1] + z * m[2][1], x * m[0][2] + y * m[1][2] + z * m[2][2]);
    }

    public Vertex mul(Vertex v)
    {
        return mul(v.x, v.y, v.z);
    }

    public Matrix3 add(Matrix3 add)
    {
        Matrix3 matrix3 = new Matrix3();

        for (int i = 0; i < this.m.length; i++)
        {
            for (int j = 0; j < this.m[0].length; j++)
            {
                matrix3.m[i][j] = this.m[i][j] + add.m[i][j];
            }
        }

        return matrix3;
    }
}
