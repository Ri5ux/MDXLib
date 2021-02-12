package com.asx.mdx.lib.client.util;

public class Normal
{
    public float x, y, z;

    public Normal(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Normal(String[] value)
    {
        x = Float.parseFloat(value[0]);
        y = Float.parseFloat(value[1]);
        z = Float.parseFloat(value[2]);
    }

    public Normal(Vertex o, Vertex a, Vertex b)
    {
        float a_x = a.x - o.x;
        float a_y = a.y - o.y;
        float a_z = a.z - o.z;

        float b_x = b.x - o.x;
        float b_y = b.y - o.y;
        float b_z = b.z - o.z;

        x = a_y * b_z - a_z * b_y;
        y = a_z * b_x - a_x * b_z;
        z = a_x * b_y - a_y * b_x;

        float norme = (float) Math.sqrt(x * x + y * y + z * z);

        x /= norme;
        y /= norme;
        z /= norme;
    }
}
