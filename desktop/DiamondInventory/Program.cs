using Microsoft.UI.Dispatching;
using Microsoft.UI.Xaml;
using WinRT;

namespace DiamondInventory;

public class Program
{
    [STAThread]
    static void Main(string[] args)
    {
        ComWrappersSupport.InitializeComWrappers();
        Application.Start((p) =>
        {
            DispatcherQueueSyncContext.SetDispatcherQueue(
                DispatcherQueue.GetForCurrentThread());
            new App();
        });
    }
}
