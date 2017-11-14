local Json2LuaParser = class("Json2LuaParser")

Json2LuaParser.result = ""
Json2LuaParser.spaceCount = 0

function Json2LuaParser:startParser(jsonFile)
	--local jsonFile = "ui/ccsUi/roleWin.ExportJson"
	self:exportFile(jsonFile)
end

function Json2LuaParser:exportFile(jsonFile)
	local fileUtil = cc.FileUtils:getInstance()
	local fullPath = fileUtil:fullPathForFilename(jsonFile)
	print(fullPath)
	local jsonStr = fileUtil:getStringFromFile(fullPath)
	local jsonVal = json.decode(jsonStr)
	self.result = "local Clz = {}\n\nClz.createNode = function() return "
	local objectValue = self:writeObject(jsonVal)
	self.result = self.result ..objectValue
	self.result = self.result.."\nend\nreturn Clz"
	local pathinfo  = io.pathinfo(fullPath)
	local name = pathinfo.basename.."/test"
	local path = pathinfo.dirname..name..".lua"
	print(path)
	local file = io.open(path,"w")
	file:write(self.result)
	file:flush()
	file:close()
end

function Json2LuaParser:getCurrentSpaceString()
	local result = ""
	for i = 1,self.spaceCount do
		result = result.."	"
	end
	return result
end

function Json2LuaParser:writeObject(object)
	local result = "{\n"
	self.spaceCount = self.spaceCount + 1
	for key,value in pairs(object) do
		local resultKey = self:writeKey(key)
		local resultValue = self:writeValue(value)
		result = result ..resultKey..resultValue..",\n"
	end
	self.spaceCount = self.spaceCount - 1
	local spaceString = self:getCurrentSpaceString()
	result = result..spaceString.."}"    
	return result
end

function Json2LuaParser:writeKey(key)
	local space = self:getCurrentSpaceString()
	local result = ""
	if type(key) == "string" then
		result = space.."[\""..tostring(key).."\"] ="
	elseif type(key) == "number" then
		result = space.."["..tostring(key).."] ="
	end
	
	return result
end

function Json2LuaParser:writeValue(value)
	if value == nil then
		return tostring(value)
	end
	if type(value) == "string" then
		return "\""..tostring(value).."\""
	end

	if type(value) == "boolean" then
		return tostring(value)
	end

	if type(value) == "table" then
		return self:writeObject(value)
	end

	if type(value) == "number" then
		return value
	end

	return ""
end

return Json2LuaParser