using System;
using System.Collections;
using System.Collections.Generic;

namespace c_sharp_interop
{
    public static class Ext
    {
        public static string NextString(this Random random, ushort length)
        {
            var buffer = new byte[length];
            random.NextBytes(buffer);
            return Convert.ToBase64String(buffer);
        }

        public static string Join(this string separator, IEnumerable<string> values)
        {
            return string.Join(separator, values);
        }

        public static string Join(this string separator, params string[] values)
        {
            return string.Join(separator, values);
        }
    }
}
