require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'

Pod::Spec.new do |s|
  s.name         = "appmetrica-react-native-analytics"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  # sdk_min_ios is the highest s.ios.deployment_target of the AppMetricaAnalytics
  # versions the dependency below can resolve. Read it from that version's podspec.
  # ios/appmetrica-sdk may contain a newer SDK than this dependency range.
  # The pod links that SDK and imports React, so the deployment target is the
  # higher of sdk_min_ios and React Native's min_ios_version_supported.
  # A target below the SDK fails the link. A target below the React module fails the import.
  sdk_min_ios = '13.0'
  min_ios = sdk_min_ios
  if Gem::Version.new(min_ios_version_supported) > Gem::Version.new(min_ios)
    min_ios = min_ios_version_supported
  end
  s.platforms    = { :ios => min_ios }
  s.source       = { :git => "https://github.com/appmetrica/appmetrica-react-native-plugin.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"

  s.dependency "AppMetricaAnalytics", ">= 5.16.0", "< 7.0.0"

  install_modules_dependencies(s)
end
