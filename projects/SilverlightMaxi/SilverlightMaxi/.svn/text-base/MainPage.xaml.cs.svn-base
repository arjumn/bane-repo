using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Ink;
using System.Collections.ObjectModel;
using Microsoft.Maps.MapControl;
using Microsoft.Maps.Engines;

using System.ServiceModel;
using SilverlightMaxi.DatabaseReference;
using Visifire.Charts;
using Visifire.Gauges;

namespace SilverlightMaxi
{
    public partial class MainPage : UserControl
    {
        ListBoxItem prevCountry;
        List<string> selectedFilters;
        List<string> selectedCountries;
        List<Chart> charts;
        int sliderYear;
        Dictionary<string, ObservableCollection<FilterValues>> gFilterValues;
        public MainPage()
        {
            InitializeComponent();
        }

        private void InitializeData(object sender, RoutedEventArgs e)
        {
            selectedFilters = new List<string>();
            selectedCountries = new List<string>();
            prevCountry = new ListBoxItem();
            gFilterValues = new Dictionary<string, ObservableCollection<FilterValues>>();
            charts = new List<Chart>();
            getInitialData();
        }

        private void getInitialData()
        {
            DatabaseServiceClient databaseClient = new DatabaseServiceClient();
            databaseClient.getInitialDataCompleted += new EventHandler<getInitialDataCompletedEventArgs>(databaseClient_getInitialDataCompleted);
            databaseClient.getInitialDataAsync();
        }

        private void databaseClient_getInitialDataCompleted(object sender, getInitialDataCompletedEventArgs e)
        {
            InitialData initialData = e.Result;
            updateMap(initialData);
            createFilters(initialData.filters);
            updateYear(initialData.filters);
            createCountries(initialData.countries);
            gFilterValues.Add("greenness", initialData.greennessValues);
        }

        /*********************************************************************************/
        // updates the map by placing pushpins for specific countries
        void updateMap(InitialData initialData)
        {
            foreach (Country country in initialData.countries)
            {
                Pushpin pushPin = new Pushpin();
                pushPin.Name = country.Name;
                pushPin.Background = new SolidColorBrush(Colors.Black);
                pushPin.FontSize = 10;
                pushPin.Location = new Location(country.Lat, country.Lon);
                pushPin.Content = getPushpinContent(country.Name, initialData.greennessValues);

                MyMap.Children.Add(pushPin);

                pushPin.MouseEnter += new MouseEventHandler(pushPin_MouseEnter);
                pushPin.MouseLeave += new MouseEventHandler(pushPin_MouseLeave);
            }
        }

        void pushPin_MouseLeave(object sender, MouseEventArgs e)
        {
            pushPinPopup.Visibility = Visibility.Collapsed;
        }

        void pushPin_MouseEnter(object sender, MouseEventArgs e)
        {
            Pushpin myPushPin = sender as Pushpin;
            pushPinHeading.Text = myPushPin.Name + " - " + getYearFromSlider();
            GreennessValue.Text = "Greenness %: " + getPushpinContent(myPushPin.Name, gFilterValues["greenness"]);
            pushPinTextBlock.Text = getPopupData(myPushPin.Name);
            MapLayer.SetPosition(pushPinPopup, myPushPin.Location);
            pushPinPopup.Visibility = Visibility.Visible;
        }

        void updatePushpinContent()
        {
            if (MyMap == null)
                return;
            for (int i = 1; i < MyMap.Children.Count; i++)
            {
                Pushpin pin = (Pushpin)MyMap.Children.ElementAt(i);
                pin.Content = getPushpinContent(pin.Name, gFilterValues["greenness"]);
            }
        }

        Int32 getYearFromSlider()
        {
            return Convert.ToInt32(String.Format("{0}", yearSlider.Value));
        }

        string getPopupData(string pushPinName)
        {
            string popupText = "";
            foreach (KeyValuePair<string, ObservableCollection<FilterValues>> entry in gFilterValues)
            {
                string filterName = entry.Key;
                if (filterName != "greenness")
                {
                    IEnumerable<FilterValues> fValues = (from x in entry.Value where (x.Country == pushPinName && x.Year == sliderYear) select x);
                    if (fValues.Count() == 0)
                        popupText += filterName + ": NA\n";
                    else
                    {
                        FilterValues filterValue = fValues.First();
                        popupText += filterName + ": " + filterValue.Value + " " + filterValue.Unit + "\n";
                    }
                }
            }
            return popupText;
        }

        int getPushpinContent(string country, ObservableCollection<FilterValues> greenValues)
        {
            int year = getYearFromSlider();
            double greennessFactor = (from o in greenValues where (o.Country == country && o.Year == year) select o).First().Value;
            
            return Convert.ToInt32(greennessFactor);
        }

        void addChartToStackPanel(Chart chart)
        {
            int addToPanel = ((charts.Count - 1) / 2) + 1;
            string stackPanelName = "chartStackPanel" + addToPanel;
            StackPanel stackPanel = chartMainStackPanel.FindName(stackPanelName) as StackPanel;
            if(stackPanel != null) stackPanel.Children.Add(chart);
        }

        void createCharts()
        {
            foreach (KeyValuePair<string, ObservableCollection<FilterValues>> entry in gFilterValues)
                drawChart(entry);
        }

        void createChart(string filterName)
        {
            foreach (KeyValuePair<string, ObservableCollection<FilterValues>> entry in gFilterValues)
                if (entry.Key == filterName)
                {
                    drawChart(entry);
                    break;
                }                                      
        }

        // creates the charts for the filters and the countries that are currently selected
        void drawChart(KeyValuePair<string, ObservableCollection<FilterValues>> entry)
        {
            int fromYear = Convert.ToInt32(fromComboBox.SelectedValue.ToString());
            int toYear = Convert.ToInt32(toComboBox.SelectedValue.ToString());

            Chart chart = new Chart();
            chart.Name = entry.Key;
            chart.Width = 400;
            chart.Height = 340;
            chart.View3D = true;

            Axis yAxis = new Axis();
            yAxis.Title = "Units";
            chart.AxesY.Add(yAxis);
            
            Title chartTitle = new Title();
            chartTitle.Text = entry.Key;
            chart.Titles.Add(chartTitle);
                
            for (int x = fromYear; x <= toYear; x++)
            {
                DataSeries dataseries = new DataSeries();
                dataseries.Name = x+"";
                dataseries.RenderAs = RenderAs.StackedColumn;
                foreach (string country in selectedCountries)
                {
                    DataPoint datapoint = new DataPoint();
                    datapoint.Name = country;
                    datapoint.YValue = getFilterValueForaCountry(entry.Value, country, x);
                    datapoint.AxisXLabel = country;
                    dataseries.DataPoints.Add(datapoint);
                }
                chart.Series.Add(dataseries);
            }
            charts.Add(chart); 
            addChartToStackPanel(chart);
        }

        void updateCharts(string country_name)
        {
            int index = findCountryIndex(charts.ElementAt(0), country_name);
            foreach (Chart chart in charts)
            {
                for (int i = 0; i < chart.Series.Count; i++)
                {
                    DataSeries ds = chart.Series.ElementAt(i);
                    ds.DataPoints.RemoveAt(index);
                }
            }
        }

        int findCountryIndex(Chart chart, string country_name)
        {
            int j;
            DataSeries ds = chart.Series.ElementAt(0);
            for (j = 0; j < ds.DataPoints.Count; j++)
            {
                DataPoint dp = ds.DataPoints.ElementAt(j);
                if (dp.Name == country_name)
                    break;
            }
            return j;
        }

        /***************************************************************************/

        private void getFilterValues(string filterName)
        {
            //MessageBox.Show("FilterValueAsync Call");
            DatabaseServiceClient databaseClient = new DatabaseServiceClient();
            databaseClient.getFilterDetailsCompleted += new EventHandler<getFilterDetailsCompletedEventArgs>(databaseClient_getFilterDetailsCompleted);
            databaseClient.getFilterDetailsAsync(filterName);
        }

        void databaseClient_getFilterDetailsCompleted(object sender, getFilterDetailsCompletedEventArgs e)
        {
            string key = e.Result.ElementAt(0).Name;
            gFilterValues.Add(key, e.Result);

            if (tabControl.SelectedIndex == 1)
                createChart(key);
        }

        double getFilterValueForaCountry(ObservableCollection<FilterValues> filterValues, string country, int year)
        {
            IEnumerable<FilterValues> values = (from o in filterValues where (o.Country == country && o.Year == year) select o);
            if (values.Count() == 0)
                return 0;
            else
                return values.First().Value;
        }

        private void sampleServiceCall() 
        {
            DatabaseServiceClient databaseClient = new DatabaseServiceClient();
            databaseClient.CountUsersCompleted += new EventHandler<CountUsersCompletedEventArgs>(databaseClient_CountUsersCompleted);
            databaseClient.CountUsersAsync();
        }

        void databaseClient_CountUsersCompleted(object sender, CountUsersCompletedEventArgs e)
        {
            MessageBox.Show(e.Result.ToString());
        }

        void proxy_CountUsersCompleted(object sender, CountUsersCompletedEventArgs e)
        {
        }

        private void addCharts() 
        {
            TextBlock sample = new TextBlock();
            sample.Height = 100;
            sample.Width = 200;
            sample.Text = "Lorem asjhdfhdhg";
            sample.Name = "sample1";
            chartStackPanel1.Children.Add(sample);
            sample = new TextBlock();
            sample.Height = 100;
            sample.Width = 200;
            sample.Text = "Lorem asjhdfhdhg";
            sample.Name = "sample2";
            chartStackPanel1.Children.Add(sample);
            sample = new TextBlock();
            sample.Height = 100;
            sample.Width = 200;
            sample.Text = "Lorem asjhdfhdhg";
            sample.Name = "sample3";
            chartStackPanel2.Children.Add(sample);
            sample = new TextBlock();
            sample.Height = 100;
            sample.Width = 200;
            sample.Text = "Lorem asjhdfhdhg";
            sample.Name = "sample4";
            chartStackPanel2.Children.Add(sample);
        }

        private void createCountries(ObservableCollection<Country> countries)
        {
            List<ListBoxItem> listBoxItems = new List<ListBoxItem>();
            ListBoxItem listBoxItem;
            int i = 0;
            int list = 1;
            foreach(Country country in countries)
            {
                listBoxItem = CreateDynamicCheckBox(country.Name, "Country" + i, new MouseButtonEventHandler(listCountryChanged), new RoutedEventHandler(checkBoxCountryChanged) ,new Thickness(5, -2, 0, 0), new Thickness(5, 5, 0, 5), 14, true);
                selectedCountries.Add(country.Name);
                listBoxItems.Add(listBoxItem);
                i++;
                if (i % 3 == 0)
                {
                    switch (list)
                    {
                        case 1:
                            countryListBox1.ItemsSource = listBoxItems;
                            listBoxItems = new List<ListBoxItem>();
                            list++;
                            break;
                        case 2:
                            countryListBox2.ItemsSource = listBoxItems;
                            listBoxItems = new List<ListBoxItem>();
                            list++;
                            break;
                        case 3:
                            countryListBox3.ItemsSource = listBoxItems;
                            listBoxItems = new List<ListBoxItem>();
                            list++;
                            break;
                        case 4:
                            countryListBox4.ItemsSource = listBoxItems;
                            listBoxItems = new List<ListBoxItem>();
                            list++;
                            break;
                        case 5:
                            countryListBox5.ItemsSource = listBoxItems;
                            listBoxItems = new List<ListBoxItem>();
                            list++;
                            break;
                    };
                }
            }
            if(listBoxItems.Count != 0)
            {
                switch (list)
                {
                    case 1:
                        countryListBox1.ItemsSource = listBoxItems;
                        break;
                    case 2:
                        countryListBox2.ItemsSource = listBoxItems;
                        break;
                    case 3:
                        countryListBox3.ItemsSource = listBoxItems;
                        break;
                    case 4:
                        countryListBox4.ItemsSource = listBoxItems;
                        break;
                    case 5:
                        countryListBox5.ItemsSource = listBoxItems;
                        break;
                };
            }
        }

        private ListBoxItem CreateDynamicCheckBox(string content, string name, MouseButtonEventHandler listItemHandler, RoutedEventHandler checkBoxHandler, Thickness padding, Thickness margin, int fontSize, bool isSelected)
        {
            ListBoxItem listBoxItem = new ListBoxItem();
            CheckBox c = new CheckBox();
            c.Content = content;
            c.FontSize = fontSize;
            c.Padding = padding;
            c.Margin = margin;
            c.IsEnabled = true;
            c.IsChecked = isSelected;
            c.Checked += checkBoxHandler;
            c.Unchecked += checkBoxHandler;
            listBoxItem.IsTabStop = false;
            listBoxItem.Content = c;
            listBoxItem.Name = name;
            listBoxItem.MouseLeftButtonUp += listItemHandler;
            return listBoxItem;
        }

        private void createFilters(ObservableCollection<Filter> filters)
        {
            List<ListBoxItem> listBoxItems = new List<ListBoxItem>();
            ListBoxItem listBoxItem;
            int i=0;
            listBoxItem = CreateDynamicCheckBox("Select All", "selectAll", new MouseButtonEventHandler(listFilterChanged), new RoutedEventHandler(filterSelectAll), new Thickness(5, -2, 0, 0), new Thickness(5, 5, 0, 5), 14, false);
            listBoxItems.Add(listBoxItem);
            foreach (Filter filter in filters)
            {
                if (filter.Name == "Greenness_Index")
                    continue;
                listBoxItem = CreateDynamicCheckBox(filter.Name, "Filter" + i , new MouseButtonEventHandler(listFilterChanged), new RoutedEventHandler(checkBoxFilterChanged)  ,new Thickness(5, -2, 0, 0), new Thickness(5, 5, 0, 5), 14, false);
                selectedFilters.Add(filter.Name);
                listBoxItems.Add(listBoxItem);
                i++;
            }
            filtersList.ItemsSource = listBoxItems;
        }

        private void addComboElements(int from, int to)
        {
            int count = 0;
            for (int i = from; i <= to; i++ )
            {
                toComboBox.Items.Add(i);
                fromComboBox.Items.Add(i);
                count++;
            }
            toComboBox.SelectedIndex = count-1;
            fromComboBox.SelectedIndex = 0;
        }

        private void updateYear(ObservableCollection<Filter> filters)
        {
            int minimum = 9999;
            int maximum = -1000;
            int value;
            foreach(Filter filter in filters)
            {
                if (filter.startYear < minimum)
                    minimum = filter.startYear;
                if(filter.endYear > maximum)
                    maximum = filter.endYear;
            }
            value = minimum;
            yearSlider.Maximum = maximum;
            yearSlider.Minimum = minimum;
            yearSlider.Value = value;
            sliderYear = value;
            sliderTickMax.Content = maximum;
            sliderTickMin.Content = minimum;

            addComboElements(minimum, maximum);
        }


        private void yearSlider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            sliderYear = Convert.ToInt32(String.Format("{0}", e.NewValue));
            //MessageBox.Show(sliderYear + "");
            updatePushpinContent();
        }

        private void filterSelectAll(object sender, RoutedEventArgs e)
        {
            CheckBox c = (CheckBox)sender; 
            ListBoxItem listBoxItem = (ListBoxItem)c.Parent;
            if (c.IsChecked == true)
            {
                foreach (ListBoxItem filterList in filtersList.Items)
                {
                    if (filterList.Name != "selectAll")
                    {
                        CheckBox cFilter = (CheckBox)filterList.Content;
                        if (cFilter.IsChecked == false)
                            cFilter.IsChecked = true;
                    }

                }
            }
            else
            {
                foreach (ListBoxItem filterList in filtersList.Items)
                {
                    if (filterList.Name != "selectAll")
                    {
                        CheckBox cFilter = (CheckBox)filterList.Content;
                        if (cFilter.IsChecked == true)
                            cFilter.IsChecked = false;
                    }

                }
            }
            listBoxItem.IsSelected = true;
        }

        private void checkBoxFilterChanged(object sender, RoutedEventArgs e)
        {
            CheckBox c = (CheckBox)sender;
            ListBoxItem listBoxItem = (ListBoxItem)c.Parent;
            listBoxItem.IsSelected = true;
            if (c.IsChecked == true)
            {
                getFilterValues(c.Content.ToString());
            }
            else
            {
                gFilterValues.Remove(c.Content.ToString());
                if (tabControl.SelectedIndex == 1)
                {
                    clearAllCharts();
                    createCharts();
                }                
            }
        }

        private void listFilterChanged(object sender, RoutedEventArgs e)
        {
            ListBoxItem selectedList = (ListBoxItem)sender;
            CheckBox c = (CheckBox)selectedList.Content;
            if (c.IsChecked == true)
            {
                c.IsChecked = false;
            }
            else
            {
                c.IsChecked = true;
            }
        }

        private void checkBoxCountryChanged(object sender, RoutedEventArgs e)
        {
            CheckBox c = (CheckBox)sender;
            ListBoxItem listBoxItem = (ListBoxItem)c.Parent;
            if (!prevCountry.Name.Equals(listBoxItem.Name))
            {
                prevCountry.IsSelected = false;
                prevCountry = listBoxItem;
            }
            if (c.IsChecked == true)
            {
                selectedCountries.Add(c.Content.ToString());
                clearAllCharts();
                createCharts();
            }
            else
            {
                selectedCountries.Remove(c.Content.ToString());
                updateCharts(c.Content.ToString());
            }
            listBoxItem.IsSelected = true;
        }

        private void listCountryChanged(object sender, RoutedEventArgs e)
        {
            ListBoxItem selectedList = (ListBoxItem)sender;
            CheckBox c = (CheckBox)selectedList.Content;
            if (c.IsChecked == true)
            {
                c.IsChecked = false;
            }
            else
            {
                c.IsChecked = true;
            }
            if (!prevCountry.Name.Equals(selectedList.Name))
            {
                prevCountry.IsSelected = false;
                prevCountry = selectedList;
            }
        }

        private List<string> getCheckedFilters()
        {
            return null;
        }

        private void alphabetLinkClicked(object sender, RoutedEventArgs e)
        {
            HyperlinkButton alphabetLink = (HyperlinkButton)sender;
            alphabetLink.IsEnabled = false;
            //prevAlphabet.IsEnabled = true;
            //prevAlphabet = alphabetLink;
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            string temp = "";
            foreach (string name in selectedFilters)
                temp += name + ":";
            MessageBox.Show(temp);
        }

        private void chartYearChanged(object sender, SelectionChangedEventArgs e)
        {
            if (fromComboBox.SelectedValue == null || toComboBox.SelectedValue == null)
                return;
            int fromValue = Convert.ToInt32(fromComboBox.SelectedValue.ToString());
            int toValue = Convert.ToInt32(toComboBox.SelectedValue.ToString());
            if (fromValue > toValue)
            {
                yearErrorLabel.Visibility = Visibility.Visible;
            }
            else
            {
                yearErrorLabel.Visibility = Visibility.Collapsed;
                if (tabControl.SelectedIndex == 1)
                {
                    clearAllCharts();
                    createCharts();
                }
            }
        }

        private void tabSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            TabControl tabControl = (TabControl)sender;
            /*if (tabControl.SelectedIndex == 1)
            {
                clearAllCharts();
                createCharts();
            }*/
        }

        private void button1_Click_1(object sender, RoutedEventArgs e)
        {
            clearAllCharts();
        }

        private void clearAllCharts()
        {
            for (int i = 1; i <= 6; i++)
            {
                string stackPanelName = "chartStackPanel" + i;
                StackPanel tempStackPanel = chartMainStackPanel.FindName(stackPanelName) as StackPanel;
                int count = tempStackPanel.Children.Count;
                for (int j = count; j > 0; j--)
                {
                    tempStackPanel.Children.RemoveAt(j - 1);
                }

            }    
            charts.Clear();
        }

        private void scrollViewerLoaded(object sender, RoutedEventArgs e)
        {
            clearAllCharts();
            createCharts();
        }
    }

    public class DiscreteSlider : Slider
    {
        protected override void OnValueChanged(double oldValue, double newValue)
        {
            if (!m_busy)
            {
                m_busy = true;
 
                if (SmallChange != 0)
                {
                    double newDiscreteValue = (int)(Math.Round(newValue / SmallChange)) * SmallChange;
 
                    if (newDiscreteValue != m_discreteValue)
                    {
                        Value = newDiscreteValue;
                        base.OnValueChanged(m_discreteValue, newDiscreteValue);
                        m_discreteValue = newDiscreteValue;
                    }
                }
                else
                {
                    base.OnValueChanged(oldValue, newValue);
                }
 
                m_busy = false;
            }
        }
 
        bool m_busy;
        double m_discreteValue;
    }    
}
