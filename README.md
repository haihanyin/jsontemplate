# jsontemplate
## Example
### Simple types
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s
}
</pre></td><td><pre>
{
  "aField" : "ISCZd"
}
</pre></td></tr>
<tr><td><pre>
{
  aField : @i
}
</pre></td><td><pre>
{
  "aField" : 56
}
</pre></td></tr>
<tr><td><pre>
{
  aField : @b
}
</pre></td><td><pre>
{
  "aField" : false
}
</pre></td></tr>
</table>
<b>@</b> is a type indicator, <b>@s</b> refers to the string type. 
The length of the generated string by default is 5.

### Simple types with a fixed value
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(myValue)
}
</pre></td><td><pre>
{
  "aField" : "myValue"
}
</pre></td></tr>
</table>
The string type <b>@s</b> is parameterized with a single value <i>myValue</i>. The generated
string is a fixed value <i>myValue</i>.

### Simple types with enumerations
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(A, B, C, D)
}
</pre></td><td><pre>
{
  "aField" : "C"
}
</pre></td></tr>
</table>
The string type <b>@s</b> is parameterized with a list value. The generated
string is one of the value enumerated in the list.

### Simple types with configuration
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(size=10)
}
</pre></td><td><pre>
{
  "aField" : "awpVXpJTxb"
}
</pre></td></tr>
<tr><td><pre>
{
  aField : @s(min=10, max=20)
}
</pre></td><td><pre>
{
  "aField" : "awpVXpJTxb"
}
</pre></td></tr>
</table>
The string type <b>@s</b> is parameterized with a named value <i>size=10</i>. The lenght of 
the generated string is configured to be 10.
</td></tr>

### Object type
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  anObject : {
    aField : @s, 
    bField : @s
  }
}
</pre></td><td><pre>
{
  "anObject" : {
    "aField" : "hhnNc",
    "bField" : "EyHbB"
  }
}
</pre></td></tr>
</table>


### Array type with size configuration
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@s[](3)
</pre></td><td><pre>
[ "hwhCL", "tDcPO", "OgdGC" ]
</pre></td></tr>
<tr><td><pre>
@s[](1, 10)
</pre></td><td><pre>
[ "QwWxg", "ytaGY", "NGZBr", "DsBKx", "MvwSb", "qsEXA", "YHkxC" ]
</pre></td></tr>
</table>

### Array type with elements
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@s [ 1, 2, 3, 4 ]
</pre></td><td><pre>
[ "1", "2", "3", "4" ]
</pre></td></tr>
<tr><td><pre>
@s [ 1, 2, 3, 4 ](6)
</pre></td><td><pre>
[ "1", "2", "3", "4", "qRTWm", "RTBik" ]
</pre></td></tr>
<tr><td><pre>
@s [ 1, @i(2), @b(false), @s(4) ]
</pre></td><td><pre>
[ "1", 2, false, "4" ]
</pre></td></tr>
</table>

### Customized type
<tr><td><pre>
{
  @address : {
    city : @s,
    street : @s,
    number : @i
  },
  office : @address, 
  home : @address
}
</pre></td><td><pre>
{
  "office" : {
    "city" : "MavBr",
    "street" : "odcjd",
    "number" : 79
  },
  "home" : {
    "city" : "zdNCm",
    "street" : "UsBcv",
    "number" : 63
  }
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
{ 
  @address : {
    city : @s(Amsterdam, Utrecht),
    street : @s,
    number : @i(min=1000)
  },
  office : @address,
  home : @address
}
</pre></td><td><pre>
{
  "office" : {
    "city" : "Amsterdam",
    "street" : "LAUbf",
    "number" : 1626
  },
  "home" : {
    "city" : "Utrecht",
    "street" : "xpLYB",
    "number" : 1024
  }
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>

### Other pre-installed types
<tr><td><pre>
{
  ipField : @ip
}
</pre></td><td><pre>
{
  "ipField" : "59.221.49.83"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>

### Default type
<tr><td><pre>
@s {
  fieldA, 
  fieldB
}
</pre></td><td><pre>
{
  "fieldA" : "yUiIE",
  "fieldB" : "vrMwv"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
@s(size=10) {
  fieldA, 
  fieldB : @s(size=20)
}
</pre></td><td><pre>
{
  "fieldA" : "yUiIE",
  "fieldB" : "vrMwv" // todo
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>

## Inject variables
### Use the variable indicator in value specification

<tr><td><pre>
{
  name : $name
}
</pre></td><td><pre>
{
  "name" : "John"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
{
  letters : $letters
}
</pre></td><td><pre>
{
  "letters" : [
    "A",
    "B",
    "C"
  ]
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
{
  person : $person
}
</pre></td><td><pre>
{
  "person" : {
    "roles" : [
      "Admin",
      "Finance",
      "HR"
    ],
    "name" : "John",
    "age" : 20,
    "male" : true
  }
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>

### Use the variable indicator in type specification
<tr><td><pre>
{
  aField: @s($myValue)
}
</pre></td><td><pre>
{
  "aField" : "helloworld"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
{
  aField: @s($myValue)
}
</pre></td><td><pre>
{
  "aField" : "C"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>


<tr><td><pre>
{
  aField: @s($config)
}
</pre></td><td><pre>
{
  "aField" : "HORklISFDrQzhumRojWQ"
}
</pre></td></tr><tr><td colspan="2">
comments</td></tr>

 ### use String.format()
 
 
</table>


