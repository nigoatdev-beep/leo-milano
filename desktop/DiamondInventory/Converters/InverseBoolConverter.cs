using Microsoft.UI.Xaml;
using Microsoft.UI.Xaml.Data;

namespace DiamondInventory.Converters;

public class InverseBoolConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, string language)
        => value is bool b ? !b : Visibility.Collapsed;

    public object ConvertBack(object value, Type targetType, object parameter, string language)
        => value is Visibility v ? v != Visibility.Visible : false;
}
