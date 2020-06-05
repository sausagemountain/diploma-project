using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading;

namespace c_sharp_interop
{
    public class Registrator
    {
    #region Singleton

        private Registrator() { }

        private static readonly Lazy<Registrator> _instance = new Lazy<Registrator>(
            () => {
                var reg = new Registrator();
                reg.AddMethod(
                    typeof (Registrator).GetMethod(
                        nameof (AddModule),
                        new[] { typeof (string), typeof (string) }
                    )
                );
                reg.AddMethod(
                    typeof (Registrator).GetMethod(
                        nameof (AddModule),
                        new[] { typeof (string), typeof (string), typeof (string) }
                    )
                );
                reg.AddMethod(typeof (Registrator).GetMethod(nameof (RemoveModule)));
                reg.AddMethod(typeof (Generator).GetMethod(nameof (Generator.Id)));
                return reg;
            },
            LazyThreadSafetyMode.ExecutionAndPublication
        );

        public static Registrator Instance => _instance.Value;

    #endregion

    #region local

        public IDictionary<string, IEnumerable<string>> LocalClassNames {
            get {
                return LocalMethods.Select(m => m.DeclaringType?.Name).
                                    Distinct().
                                    ToDictionary(
                                        c => c,
                                        c => LocalMethods.
                                             Where(m => m.DeclaringType.Name == c).
                                             Select(m => m.Name)
                                    );
            }
        }

        public IDictionary<Type, IEnumerable<MethodInfo>> LocalClasses {
            get {
                return LocalMethods.Select(m => m.DeclaringType).
                                    Distinct().
                                    ToDictionary(
                                        c => c,
                                        c => LocalMethods.
                                            Where(m => m.DeclaringType == c)
                                    );
            }
        }

        private readonly LinkedList<MethodInfo> _localMethods = new LinkedList<MethodInfo>();

        public IEnumerable<MethodInfo> LocalMethods => _localMethods;

        public void AddMethod(MethodInfo method)
        {
            _localMethods.AddLast(method);
        }

        public void RemoveMethod(MethodInfo method)
        {
            _localMethods.Remove(method);
        }

    #endregion

    #region remote

        private readonly Dictionary<string, Uri> _modules = new Dictionary<string, Uri>();
        public IDictionary<string, Uri> Modules => _modules;

        private readonly Dictionary<string, Uri> _guiModules = new Dictionary<string, Uri>();
        public IDictionary<string, Uri> GuiModules => _guiModules;

        public void AddModule(string name, string uri)
        {
            _modules.Add(name, new Uri(uri));
        }

        public void AddModule(string name, string uri, string guiUri)
        {
            _modules.Add(name, new Uri(uri));
            _guiModules.Add(name, new Uri(guiUri));
        }

        public void RemoveModule(string name)
        {
            _modules.Remove(name);
            _guiModules.Remove(name);
        }

        public IDictionary<string, string> GuiList()
        {
            return GuiModules.ToDictionary(p => p.Key, p => p.Value.ToString());
        }

    #endregion
    }
}