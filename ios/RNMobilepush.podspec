
Pod::Spec.new do |s|
  s.name         = "RNMobilepush"
  s.version      = "1.0.0"
  s.summary      = "RNMobilepush"
  s.description  = <<-DESC
                  RNMobilepush
                   DESC
  s.homepage     = "https://github.com/author/RNMobilepush"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "9.0"
  s.source       = { :path => "./" }
  s.source_files  = "*.{h,m}"
  s.vendored_frameworks = "OneSDK/AlicloudUtils.framework","OneSDK/AlicloudSender.framework","OneSDK/EMASRest.framework","OneSDK/CloudPushSDK.framework","OneSDK/UTDID.framework","OneSDK/UTMini.framework"
  s.libraries = "z", "resolv", "sqlite3"
  s.requires_arc = true
  s.framework = "SystemConfiguration"
  s.framework = "CoreTelephony"
  s.dependency "React"
  #s.dependency "others"

end

