using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json.Linq;

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
            return Join(separator, values.AsEnumerable());
        }
    }

    public class Generator
    {
        private Random rand { get; } = new Random();

        public string Id(long length)
        {
            return rand.NextString((ushort) length);
        }
    }
}