using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Text;
using System.Collections.ObjectModel;

namespace SilverlightMaxi.Web
{
    public class Database
    {
        static string connectionString =
                ConfigurationManager.ConnectionStrings["DataConnection"].ConnectionString;


        public ObservableCollection<FilterValues> getFilterValues(string filter)
        {
            ObservableCollection<FilterValues> filterValues = new ObservableCollection<FilterValues>();
            SqlConnection sqlConnection = new SqlConnection(connectionString);
            DataSet mapSet = new DataSet();

            SqlCommand sqlcommand = new SqlCommand();
            sqlcommand.Connection = sqlConnection;
            sqlcommand.CommandText = "select distinct country, year, value, unit from " + filter;
            sqlcommand.CommandType = CommandType.Text;

            SqlDataAdapter sqlDataAdapter = new SqlDataAdapter();
            sqlDataAdapter.SelectCommand = sqlcommand;

            sqlDataAdapter.Fill(mapSet);

            if (mapSet.Tables.Count > 0)
            {
                foreach (DataRow dataRow in mapSet.Tables[0].Rows)
                {
                    filterValues.Add(new FilterValues()
                    {
                        Name = filter,
                        Country = dataRow["country"].ToString(),
                        Year = Convert.ToInt32(dataRow["year"]),
                        Value = Convert.ToDouble(dataRow["value"]),
                        Unit = dataRow["unit"].ToString(),
                    });
                }
            }

            return filterValues;
        }

        public ObservableCollection<Filter> getFilters()
        {
            ObservableCollection<Filter> filters = new ObservableCollection<Filter>();

            SqlConnection sqlConnection = new SqlConnection(connectionString);
            DataSet mapSet = new DataSet();

            SqlCommand sqlcommand = new SqlCommand();
            sqlcommand.Connection = sqlConnection;
            sqlcommand.CommandText = "select Name, Start_Year, End_Year from [Filter]"; //change
            sqlcommand.CommandType = CommandType.Text;

            SqlDataAdapter sqlDataAdapter = new SqlDataAdapter();
            sqlDataAdapter.SelectCommand = sqlcommand;

            sqlDataAdapter.Fill(mapSet);

            if (mapSet.Tables.Count > 0)
            {
                foreach (DataRow dataRow in mapSet.Tables[0].Rows)
                {
                    filters.Add(new Filter()
                    {
                        Name = dataRow["Name"].ToString(),
                        startYear = Convert.ToInt32(dataRow["Start_Year"]),
                        endYear = Convert.ToInt32(dataRow["End_Year"])
                    });
                }
            }
            return filters;
        }


        public ObservableCollection<Country> getCountryLatLon()
        {
            ObservableCollection<Country> countryList = new ObservableCollection<Country>();
            SqlConnection sqlConnection = new SqlConnection(connectionString);
            DataSet mapSet = new DataSet();

            SqlCommand sqlcommand = new SqlCommand();
            sqlcommand.Connection = sqlConnection;
            sqlcommand.CommandText = "select name, lat, lon from Country_Geoloc";
            sqlcommand.CommandType = CommandType.Text;

            SqlDataAdapter sqlDataAdapter = new SqlDataAdapter();
            sqlDataAdapter.SelectCommand = sqlcommand;

            sqlDataAdapter.Fill(mapSet);
            if (mapSet.Tables.Count > 0)
            {
                foreach (DataRow dataRow in mapSet.Tables[0].Rows)
                {
                    countryList.Add(new Country()
                    {
                        Name = dataRow["Name"].ToString(),
                        Lat = Convert.ToDouble(dataRow["lat"]),
                        Lon = Convert.ToDouble(dataRow["lon"])
                    });
                }
            }

            return countryList;
        }
    }
}
