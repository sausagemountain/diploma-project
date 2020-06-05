using System;
using System.Collections.Generic;
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
            return string.Join(separator, values);
        }

        public static object ToNative(this JToken token)
        {
            switch (token.Type) {
                case JTokenType.Array:
                    return token.ToObject<JToken[]>();
                case JTokenType.Integer:
                    return token.ToObject<long>();
                case JTokenType.Float:
                    return token.ToObject<double>();
                case JTokenType.String:
                    return token.ToObject<string>();
                case JTokenType.Boolean:
                    return token.ToObject<bool>();
                case JTokenType.Date:
                    return token.ToObject<DateTime>();
                case JTokenType.Bytes:
                    return token.ToObject<byte[]>();
                case JTokenType.Guid:
                    return token.ToObject<Guid>();
                case JTokenType.Uri:
                    return token.ToObject<Uri>();
                case JTokenType.TimeSpan:
                    break;
                case JTokenType.Raw:
                case JTokenType.Comment:
                case JTokenType.Constructor:
                case JTokenType.Property:
                case JTokenType.Object:
                    return token;
                case JTokenType.Null:
                case JTokenType.Undefined:
                    return null;
            }

            return null;
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