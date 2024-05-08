select test.*, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit
from test
join result on result.test_id = test.test_id
join parameter on parameter.name = result.parameter_name;

