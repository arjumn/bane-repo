using System;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.Collections.ObjectModel;
using System.Collections.Generic;

namespace SilverlightMaxi.Web
{
    [ServiceContract(Namespace = "")]
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    public class DatabaseService
    {
        private Database database;

        [OperationContract]
        public int CountUsers()
        {
            return 2;
        }
        
        [OperationContract]
        public InitialData getInitialData()
        {
            InitialData initialData = new InitialData()
                                          {
                                              countries = getCountryLatLon(),
                                              filters = getFilters(),
                                              greennessValues = getFilterDetails("Greenness_Index"),
                                          };

            return initialData;
        }

        [OperationContract]
        public ObservableCollection<FilterValues> getFilterDetails(string filterName)
        {
            database = new Database();
            return database.getFilterValues(filterName);
        }

        private ObservableCollection<Filter> getFilters()
        {
            database = new Database();
            return database.getFilters();
        }
        
        
        private ObservableCollection<Country> getCountryLatLon()
        {
            database = new Database();
            return database.getCountryLatLon();
        }
    }
}
