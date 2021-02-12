package com.asx.mdx.lib.client.util;

public class Matrix4
{
    private float[] matrix;
    public static final int SIZE = 4;

    public Matrix4()
    {
        this.matrix = new float[SIZE * SIZE];
        this.setIdentity();
    }

    public Matrix4(float[] arr)
    {
        int n = SIZE * SIZE;

        if (arr.length != n)
        {
            throw new RuntimeException("Matrix4 constructor needs a double array of " + n + " elements");
        }

        matrix = arr;
    }

    public void clone(Matrix4 other)
    {
        matrix = new float[SIZE * SIZE];

        for (int i = 0; i < matrix.length; i++)
        {
            matrix[i] = other.matrix[i];
        }
    }

    public void setIdentity()
    {
        for (int i = 0; i < matrix.length; i++)
        {
            matrix[i] = 0;
        }

        for (int i = 0; i < SIZE; i++)
        {
            matrix[i * SIZE + i] = 1;
        }
    }

    public Matrix4 mul(Matrix4 right)
    {
        Matrix4 result = new Matrix4();
        int idx = 0;
        float r;

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                r = 0;

                for (int k = 0; k < SIZE; k++)
                {
                    r += this.matrix[i * SIZE + k] * right.matrix[k * SIZE + j];
                }

                result.matrix[idx] = r;
                idx++;
            }
        }

        return result;
    }

    public float get(int col, int row)
    {
        return matrix[row * 4 + col];
    }

    public float[] transform(float x, float y, float z, float w)
    {
        float[] r = new float[4];
        float[] m = this.matrix;
        int idx = 0;

        for (int i = 0; i < r.length; ++i)
        {
            r[i] = m[idx] * x + m[idx + 1] * y + m[idx + 2] * z + m[idx + 3] * w;
            idx += SIZE;
        }

        return r;
    }

    public String toString()
    {
        String r = "[Matrix4]\n";
        int idx = 0;

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                r += " " + matrix[idx++];
            }
            r += "\n";
        }

        return r;
    }
}
